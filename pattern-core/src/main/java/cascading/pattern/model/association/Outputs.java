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
package cascading.pattern.model.association;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cascading.tuple.Fields;

/**
 * A set of outputs. The fieldIds shoud all be unique
 * 
 * @author jamesiry
 *
 * @param <FieldId>
 */
public class Outputs<FieldId extends Comparable<FieldId>> implements Serializable
  {
  /**
   * Set<Output> managed by this Outputs specification
   */
  private final Set<Output<FieldId>> outputs;
  /**
   * Precomputed Set<RuleMatcher> extracted from outputs
   */
  private final Set<RuleMatcher> ruleMatchers = new LinkedHashSet<RuleMatcher>();

  public Outputs( Set<Output<FieldId>> outputs )
    {
    super();
    this.outputs = outputs;

    for( Output<?> output : outputs )
      {
      ruleMatchers.add( output.getMatcher() );
      }
    }

  Set<RuleMatcher> ruleMatchers()
    {
    return Collections.unmodifiableSet( ruleMatchers );
    }

  /**
   * Extract a Map from FieldId to rule feature given a Map from RuleMatcher to
   * List of AssociationRule
   * 
   * @param map
   * @return
   */
  public <Item> Map<FieldId, Object> extract( Map<RuleMatcher, List<AssociationRule<Item>>> map )
    {
    Map<FieldId, Object> result = new HashMap<FieldId, Object>();
    for( Output<FieldId> output : outputs )
      {
      final Object value = output.extract( map );
      result.put( output.getFieldId(), value );
      }
    return result;
    }

  Fields fields()
    {
    ArrayList<Fields> fields = new ArrayList<Fields>( outputs.size() );
    for( Output<FieldId> output : outputs )
      {
      fields.add( new Fields( output.getFieldId(), output.getType() ) );
      }
    Fields[] x = new Fields[ 0 ];
    return Fields.join( fields.toArray( x ) );
    }

  Iterable<Output<FieldId>> outputs()
    {
    return Collections.unmodifiableCollection( outputs );
    }

  }
