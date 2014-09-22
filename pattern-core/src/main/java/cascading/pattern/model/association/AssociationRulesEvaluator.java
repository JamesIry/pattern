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
import java.util.Map;

public class AssociationRulesEvaluator<FieldId extends Comparable<FieldId>, Item> implements Serializable
  {
  private final AssociationRules<Item> rules;
  private final Outputs<FieldId> outputs;

  public AssociationRulesEvaluator( Iterable<AssociationRule<Item>> rules, Outputs<FieldId> outputs )
    {
    this.rules = new AssociationRules<Item>( rules );
    this.outputs = outputs;
    }

  /**
   * Evaluate an ItemSet based on the rules and outputs in this Evaluator
   * 
   * @param items
   *          input ItemSet to be evaluated
   * @param outputs
   *          specification of what fields to produce and how they are to be
   *          produced (see Output and Outputs)
   * @return A map from a FieldId to a single result
   */
  public Map<FieldId, Object> evaluate( ItemSet<Item> items )
    {
    return outputs.extract( rules.evaluate( items, outputs.ruleMatchers() ) );
    }

  }
