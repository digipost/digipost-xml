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

import no.digipost.xml.validation.SchemaHelper;

import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;

import java.util.Optional;
import java.util.Set;

@FunctionalInterface
public interface MarshallerCustomizer {

    final MarshallerCustomizer NO_CUSTOMIZATION = new NoCustomization("MarshallerCustomizer.NO_CUSTOMIZATION");

    public static MarshallerCustomizer validateUsingSchemaResources(Set<String> schemaResources) {
        return Optional.ofNullable(schemaResources)
                .filter(s -> !s.isEmpty())
                .map(SchemaHelper::createW3cXmlSchema)
                .map(MarshallerCustomizer::validateUsingSchema)
                .orElse(NO_CUSTOMIZATION);
    }

    public static MarshallerCustomizer validateUsingSchema(Schema schema) {
        return marshaller -> marshaller.setSchema(schema);
    }



    void customize(Marshaller marshaller) throws Exception;


    default MarshallerCustomizer andThen(MarshallerCustomizer nextCustomization) {
        if (nextCustomization instanceof NoCustomization) {
            return this;
        } else {
            return marshaller -> {
                this.customize(marshaller);
                nextCustomization.customize(marshaller);
            };
        }
    }

}
