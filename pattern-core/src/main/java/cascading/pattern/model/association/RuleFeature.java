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

/**
 * A feature (field) of an AssociationRule to be output
 */
public enum RuleFeature implements Serializable
  {
  /**
   * An AssociationRule's antecedent field
   */
  ANTECEDENT
    {
    @Override
    public <Item> ItemSet<Item> extract( AssociationRule<Item> rule )
      {
      return rule.getAntecedent();
      }
    },
  /**
   * An AssociationRule's consequentfield
   */
  CONSEQUENT
    {
    @Override
    public <Item> ItemSet<Item> extract( AssociationRule<Item> rule )
      {
      return rule.getConsequent();
      }
    },
  /**
   * An AssociationRule's support field
   */
  SUPPORT
    {
    @Override
    public <Item> Double extract( AssociationRule<Item> rule )
      {
      return rule.getSupport();
      }
    },
  /**
   * An AssociationRule's confidence field
   */
  CONFIDENCE
    {
    @Override
    public <Item> Double extract( AssociationRule<Item> rule )
      {
      return rule.getConfidence();
      }
    },
  /**
   * An AssociationRule's lift field
   */
  LIFT
    {
    @Override
    public <Item> Double extract( AssociationRule<Item> rule )
      {
      return rule.getLift();
      }
    },
  /**
   * An AssociationRule's leverage field
   */
  LEVERAGE
    {
    @Override
    public <Item> Double extract( AssociationRule<Item> rule )
      {
      return rule.getLeverage();
      }
    },
  /**
   * An AssociationRule's affinity field
   */
  AFFINITY
    {
    @Override
    public <Item> Double extract( AssociationRule<Item> rule )
      {
      return rule.getAffinity();
      }
    },
  /**
   * An AssociationRule's id field
   */
  ID
    {
    @Override
    public <Item> String extract( AssociationRule<Item> rule )
      {
      return rule.getId();
      }
    },
  RULE
    {
    @Override
    public <Item> AssociationRule<Item> extract( AssociationRule<Item> rule )
      {
      return rule;
      }
    };

  /**
   * Extract the value of one field from a rule
   * 
   * @param rule
   * @return
   */
  public abstract <Item> Object extract( AssociationRule<Item> rule );
  }
