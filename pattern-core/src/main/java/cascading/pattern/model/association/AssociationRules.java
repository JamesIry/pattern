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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A collection of association rules
 * 
 * @param <Item>
 *          The type of item the underlying association rules can understand
 */
public class AssociationRules<Item> implements Serializable
  {
  /**
   * The rules in this set of AssociationRules
   */
  private final Set<AssociationRule<Item>> rules = new LinkedHashSet<AssociationRule<Item>>();

  /**
   * Constructs a new AssociationRules based on a collection of rules
   * 
   * @param rules
   */
  public AssociationRules( Iterable<AssociationRule<Item>> rules )
    {
    super();
    for( AssociationRule<Item> rule : rules )
      {
      this.rules.add( rule );
      }
    }

  /**
   * Constructs a new AssociationRules based on a collection of rules
   * 
   * @param rules
   */
  public AssociationRules( AssociationRule<Item>... rules )
    {
    this( Arrays.asList( rules ) );
    }

  /**
   * Evaluate an ItemSet against this set of rules based on a specified
   * RuleMatcher, return a List of AssociationRules that match
   * 
   * @param items
   *          input ItemSet to be evaluated
   * @param matcher
   *          algorithm used to determine which rules match
   * @return list of AssociationRule that match according to the matcher
   */
  public List<AssociationRule<Item>> evaluate( ItemSet<Item> items, RuleMatcher matcher )
    {
    List<AssociationRule<Item>> result = new ArrayList<AssociationRule<Item>>();

    for( AssociationRule<Item> rule : rules )
      if( matcher.matches( rule, items ) )
        result.add( rule );

    return result;
    }

  /**
   * Evaluate an ItemSet against a set of RuleMatchers
   * 
   * @param items
   *          input ItemSet to be evaluated
   * @param matcher
   *          algorithms used to determine which rules match
   * @return A map from matcher to list of AssociationRule that match according
   *         to that matcher
   */
  public Map<RuleMatcher, List<AssociationRule<Item>>> evaluate( ItemSet<Item> items, Set<RuleMatcher> matchers )
    {
    Map<RuleMatcher, List<AssociationRule<Item>>> result = new HashMap<RuleMatcher, List<AssociationRule<Item>>>();
    for( RuleMatcher matcher : matchers )
      result.put( matcher, evaluate( items, matcher ) );

    return result;
    }
  }
