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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RuleFeatureTest
  {
  private final ItemSet<String> set1 = new ItemSet<String>( "foo", "baz", "num" );
  private final ItemSet<String> set2 = new ItemSet<String>( "foo", "baz" );

  private final AssociationRule<String> rule = new AssociationRule<String>( "rule1", set1, set2, 100.0, 200.0, 300.0,
      400.0, 500.0 );

  @Test
  public void test()
    {
    verify( "rule1", RuleFeature.ID );
    verify( set1, RuleFeature.ANTECEDENT );
    verify( set2, RuleFeature.CONSEQUENT );
    verify( rule, RuleFeature.RULE );
    verify( 100.0, RuleFeature.SUPPORT );
    verify( 200.0, RuleFeature.CONFIDENCE );
    verify( 300.0, RuleFeature.LIFT );
    verify( 400.0, RuleFeature.LEVERAGE );
    verify( 500.0, RuleFeature.AFFINITY );
    }

  private void verify( Object value, RuleFeature feature )
    {
    assertEquals( value, feature.extract( rule ) );
    }
  }
