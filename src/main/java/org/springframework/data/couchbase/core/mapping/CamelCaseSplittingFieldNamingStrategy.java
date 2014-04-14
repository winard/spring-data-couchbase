/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.couchbase.core.mapping;

import org.springframework.data.util.ParsingUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Configurable {@link FieldNamingStrategy} that splits up camel-case property names and reconcatenates them using a
 * configured delimiter. Individual parts of the name can be manipulated using {@link #preparePart(String)}.
 *
 * @author Oliver Gierke
 * @author Michael Nitschinger
 * @since 1.1
 */
public class CamelCaseSplittingFieldNamingStrategy implements FieldNamingStrategy {

  private final String delimiter;

  /**
   * Creates a new {@link CamelCaseSplittingFieldNamingStrategy}.
   *
   * @param delimiter must not be {@literal null}.
   */
  public CamelCaseSplittingFieldNamingStrategy(String delimiter) {
    Assert.notNull(delimiter, "Delimiter must not be null!");
    this.delimiter = delimiter;
  }

  @Override
  public String getFieldName(final CouchbasePersistentProperty property) {
    List<String> parts = ParsingUtils.splitCamelCaseToLower(property.getName());
    List<String> result = new ArrayList<String>();

    for (String part : parts) {
      String candidate = preparePart(part);

      if (StringUtils.hasText(candidate)) {
        result.add(candidate);
      }
    }

    return StringUtils.collectionToDelimitedString(result, delimiter);
  }

  /**
   * Callback to prepare the uncapitalized part obtained from the split up of the camel case source. Default
   * implementation returns the part as is.
   *
   * @param part the part.
   * @return the prepared part.
   */
  protected String preparePart(String part) {
    return part;
  }

}
