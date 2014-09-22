package cascading.pattern.model.association;

/*
 * Copyright (c) 2014 Concurrent, Inc. All Rights Reserved.
 *
 * Project and contact information: http://www.cascading.org/
 *
 * This file is part of the Cascading project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

import cascading.pattern.datafield.DataField;
import cascading.pattern.model.ModelSchema;
import cascading.pipe.Every;
import cascading.pipe.GroupBy;
import cascading.pipe.Pipe;
import cascading.pipe.SubAssembly;
import cascading.tuple.Fields;

public class AssociationRulesAssembly<Item, FieldId extends Comparable<FieldId>> extends SubAssembly
  {

  public static <Item, FieldId extends Comparable<FieldId>> AssociationRulesAssembly<Item, FieldId> fromModelSchema(
      Pipe input, ModelSchema schema, Set<AssociationRule<Item>> rules, Set<Output<FieldId>> outputs )
    {
    List<String> expectedFieldNames = schema.getExpectedFieldNames();
    if( expectedFieldNames.size() != 1 )
      throw new UnsupportedOperationException(
          "Association  data must have one 'expected' field that specifies the item" );
    String expectedFieldName = expectedFieldNames.get( 0 );
    DataField field = schema.getExpectedField( expectedFieldName );

    @SuppressWarnings( "unchecked" )
    FieldId fieldId = (FieldId) field.getName();

    return new AssociationRulesAssembly<Item, FieldId>( input, schema.getKeyFields(), fieldId, field.getType(), rules,
        outputs );
    }

  public AssociationRulesAssembly( Pipe input, Fields groupByFields, FieldId itemField, Type itemType,
      Set<AssociationRule<Item>> rules, Set<Output<FieldId>> outputs )
    {
    super( "AssociationRules", new Pipe[] { input } );

    Pipe transactionGroupBy = new GroupBy( input, groupByFields );
    Pipe itemSetBuffer = new Every( transactionGroupBy, new AssociationRulesCascadingBuffer<FieldId, Item>(
        groupByFields, itemField, itemType, rules, new Outputs<FieldId>( outputs ) ), Fields.RESULTS );

    setTails( itemSetBuffer );
    }
  }
