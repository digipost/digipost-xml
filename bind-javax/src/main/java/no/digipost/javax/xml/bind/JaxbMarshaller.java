/*
 * Copyright (C) Posten Norge AS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package no.digipost.javax.xml.bind;

import no.digipost.xml.parsers.SaxParserProvider;
import no.digipost.xml.transform.sax.SaxInputSources;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;


public class JaxbMarshaller {

    private final JAXBContext jaxbContext;
    private final MarshallingCustomization marshallingCustomization;
    private final SaxParserProvider saxParserProvider;

    public JaxbMarshaller(MarshallingCustomization marshallingCustomization, Class<?> ... classesToBeBound) {
        this.jaxbContext = JaxbUtils.initContext(classesToBeBound);
        this.marshallingCustomization = marshallingCustomization;
        this.saxParserProvider = SaxParserProvider.createSecuredProvider();
    }

    public JaxbMarshaller(MarshallingCustomization marshallingCustomization, Set<Class<?>> classesToBeBound) {
        this(marshallingCustomization, classesToBeBound.toArray(new Class<?>[classesToBeBound.size()]));
    }

    public JaxbMarshaller(Class<?> ... classesToBeBound) {
        this(MarshallingCustomization.NO_CUSTOMIZATION, classesToBeBound);
    }

    public JaxbMarshaller(Set<Class<?>> classesToBeBound) {
        this(MarshallingCustomization.NO_CUSTOMIZATION, classesToBeBound);
    }


    public String marshalToString(Object object) {
        return marshalToResult(object, xml -> xml.toString(UTF_8.name()));
    }

    public byte[] marshalToBytes(Object object) {
        return marshalToResult(object, ByteArrayOutputStream::toByteArray);
    }

    public Document marshalToDomDocument(Object object) {
        DOMResult domResult = new DOMResult();
        doWithMarshaller(object, (o, marshaller) -> marshaller.marshal(o, domResult));
        return (Document) domResult.getNode();
    }

    public void marshal(Object object, OutputStream outputStream) {
        doWithMarshaller(object, (o, marshaller) -> marshaller.marshal(o, outputStream));
    }


    private <R> R marshalToResult(Object object, ThrowingFunction<? super ByteArrayOutputStream, ? extends R> outputStreamMapper) {
        try (ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream(128)) {
            marshal(object, xmlOutputStream);
            return outputStreamMapper.apply(xmlOutputStream);
        } catch (MarshallingException marshalException) {
            throw marshalException;
        } catch (Exception e) {
            throw MarshallingException.failedMarshal(object, e);
        }
    }

    private <T> void doWithMarshaller(T object, ThrowingBiConsumer<? super T, ? super Marshaller> operation) {
        try {
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshallingCustomization.customize(marshaller);
            operation.accept(object, marshaller);
        } catch (Exception e) {
            throw MarshallingException.failedMarshal(object, e);
        }
    }

    public <T> T unmarshal(InputStream inputStream, Class<T> type) {
        Source xmlSource = saxParserProvider.createSource(SaxInputSources.fromInputStreamPreventClose(inputStream));
        return unmarshal(unmarshaller -> unmarshaller.unmarshal(xmlSource), type);
    }

    public <T> T unmarshal(byte[] bytes, Class<T> type) {
        return unmarshal(new ByteArrayInputStream(bytes), type);
    }

    public <T> T unmarshal(Node node, Class<T> type) {
        return unmarshal(unmarshaller -> unmarshaller.unmarshal(node), type);
    }

    private <T> T unmarshal(ThrowingFunction<? super Unmarshaller, ?> operation, Class<T> type) {
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            marshallingCustomization.customize(unmarshaller);
            return type.cast(operation.apply(unmarshaller));
        } catch (Exception e) {
            throw MarshallingException.failedUnmarshal(type, e);
        }
    }


    @FunctionalInterface
    private interface ThrowingFunction<T, R> {
        R apply(T t) throws Exception;
    }

    @FunctionalInterface
    private interface ThrowingBiConsumer<T, S> {
        void accept(T t, S s) throws Exception;
    }

}
