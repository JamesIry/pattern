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
 * The algorithm used to determine if an AssociationRule matches an Item
 */
public enum RuleMatcher implements Serializable
  {
  /**
   * Match if antecedent matches
   */
  RECOMMENDATION
    {
    public <Item> boolean matches( AssociationRule<Item> rule, ItemSet<Item> items )
      {
      return ( rule.antecedentMatches( items ) );
      }
    },
  /**
   * Match if antecedent matches but consequent does not
   */
  EXCLUSIVE_RECOMMENDATION
    {

    @Override
    public <Item> boolean matches( AssociationRule<Item> rule, ItemSet<Item> items )
      {
      return ( rule.antecedentMatches( items ) && !rule.consequentMatches( items ) );
      }

    },
  /**
   * Match if both antecedent and consequent match
   */
  RULE_ASSOCIATION
    {
    @Override
    public <Item> boolean matches( AssociationRule<Item> rule, ItemSet<Item> items )
      {
      return ( rule.antecedentMatches( items ) && rule.consequentMatches( items ) );
      }
    },
  /**
   * Algorithm used in testing. Normal code shouldn't need to use it. Matches
   * all rules.
   */
  TEST_MATCH_EVERYTHING
    {
    @Override
    public <Item> boolean matches( AssociationRule<Item> rule, ItemSet<Item> items )
      {
      return true;
      }
    },
  /**
   * Algorithm used in testing. Normal code shouldn't need to use it. Matches no
   * rules.
   */
  TEST_MATCH_NOTHING
    {
    @Override
    public <Item> boolean matches( AssociationRule<Item> rule, ItemSet<Item> items )
      {
      return false;
      }
    };

  /**
   * Whether the specified rule matches the specified item according to this
   * matcher algorithm
   * 
   * @param rule
   * @param items
   * @return
   */
  public abstract <Item> boolean matches( AssociationRule<Item> rule, ItemSet<Item> items );
  }
