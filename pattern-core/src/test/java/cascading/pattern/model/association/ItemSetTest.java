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

import cascading.pattern.model.association.ItemSet;
import static org.junit.Assert.*;

public class ItemSetTest
  {
  private ItemSet<String> set1 = new ItemSet<String>( "foo", "baz", "num" );
  private ItemSet<String> set2 = new ItemSet<String>( "foo", "baz" );

  @Test
  public void testToString()
    {
    assertEquals( "{foo, baz, num}", set1.toString() );
    }

  @Test
  public void testIsSubsetOf()
    {
    assertTrue( set2.isSubsetOf( set1 ) );
    assertTrue( set1.isSubsetOf( set1 ) );
    assertTrue( set2.isSubsetOf( set2 ) );
    assertFalse( set1.isSubsetOf( set2 ) );
    }
  }
