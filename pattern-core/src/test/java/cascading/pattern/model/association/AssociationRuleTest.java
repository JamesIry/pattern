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

import org.junit.Test;

import cascading.pattern.model.association.AssociationRule;
import cascading.pattern.model.association.ItemSet;
import static org.junit.Assert.*;

public class AssociationRuleTest
  {
  private final ItemSet<String> set1 = new ItemSet<String>( "foo", "baz", "num" );
  private final ItemSet<String> set2 = new ItemSet<String>( "foo", "baz" );

  private final AssociationRule<String> rule1 = new AssociationRule<String>( "rule1", set1, set1 );
  private final AssociationRule<String> rule2 = new AssociationRule<String>( "rule2", set2, set2 );
  private final AssociationRule<String> rule3 = new AssociationRule<String>( "rule3", set1, set2 );

  @Test
  public void testToString()
    {
    assertEquals( "{foo, baz, num}->{foo, baz}", rule3.toString() );
    }

  @Test
  public void testAntecedentMatches()
    {
    assertFalse( rule1.antecedentMatches( set2 ) );
    assertTrue( rule2.antecedentMatches( set1 ) );
    assertFalse( rule3.antecedentMatches( set2 ) );
    }

  @Test
  public void testConsequentMatches()
    {
    assertFalse( rule1.consequentMatches( set2 ) );
    assertTrue( rule2.consequentMatches( set1 ) );
    assertTrue( rule3.consequentMatches( set2 ) );
    }

  @Test
  public void testDataFields()
    {
    AssociationRule<String> rule = new AssociationRule<String>( "id", set1, set2, 1, 2, 3, 4, 5 );
    assertEquals( "id", rule.getId() );
    assertEquals( 1, rule.getSupport(), 0 );
    assertEquals( 2, rule.getConfidence(), 0 );
    assertEquals( 3, rule.getLift(), 0 );
    assertEquals( 4, rule.getLeverage(), 0 );
    assertEquals( 5, rule.getAffinity(), 0 );
    }
  }