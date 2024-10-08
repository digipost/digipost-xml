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

import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class MarshallingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public static MarshallingException failedUnmarshal(Class<?> target, Throwable cause) {
        return new MarshallingException("Failed unmarshalling XML to " + target.getName(), cause);
    }

    public static MarshallingException failedMarshal(Object objectFailingToMarshal, Throwable cause) {
        return new MarshallingException(
                "Failed marshalling " + (objectFailingToMarshal != null ? objectFailingToMarshal.getClass().getName() : "null") + " to XML",
                cause);
    }

    public MarshallingException(String message, Throwable cause) {
        super(message + (cause != null ? ", because " + messageIncludingCauses(cause) : ""), cause);
    }

    private static String messageIncludingCauses(Throwable throwable) {
        return causalChainOf(throwable)
                .map(e -> e.getClass().getSimpleName() + ": '" + e.getMessage() + "'")
                .collect(joining(", caused by "));
    }

    private static Stream<Throwable> causalChainOf(Throwable t) {
        Stream.Builder<Throwable> causes = Stream.builder();
        for (Throwable cause = t; cause != null; cause = cause.getCause()) {
            causes.add(cause);
        }
        return causes.build();
    }

}
