
/*
 * The JTS Topology Suite is a collection of Java classes that
 * implement the fundamental operations required to validate a given
 * geo-spatial data set to a known topological specification.
 *
 * Copyright (C) 2001 Vivid Solutions
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * For more information, contact:
 *
 *     Vivid Solutions
 *     Suite #1A
 *     2328 Government Street
 *     Victoria BC  V8T 5G5
 *     Canada
 *
 *     (250)385-6040
 *     www.vividsolutions.com
 */

package test.jts.junit.geom;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.vividsolutions.jts.geom.Dimension;
import com.vividsolutions.jts.geom.IntersectionMatrix;


/**
 * @version 1.7
 */
public class IntersectionMatrixTest extends TestCase {

  public IntersectionMatrixTest(String name) { super(name); }

  private static int A = Dimension.A;
  private static int L = Dimension.L;
  private static int P = Dimension.P;

  public static Test suite() { return new TestSuite(IntersectionMatrixTest.class); }

  public void testToString() throws Exception {
    IntersectionMatrix i = new IntersectionMatrix();
    i.set("012*TF012");
    assertEquals("012*TF012", i.toString());

    IntersectionMatrix c = new IntersectionMatrix(i);
    assertEquals("012*TF012", c.toString());
  }

  public void testTranspose() {
    IntersectionMatrix x = new IntersectionMatrix("012*TF012");

    IntersectionMatrix i = new IntersectionMatrix(x);
    IntersectionMatrix j = i.transpose();
    assertSame(i, j);

    assertEquals("0*01T12F2", i.toString());

    assertEquals("012*TF012", x.toString());
  }

  public void testIsDisjoint() {
    assertTrue((new IntersectionMatrix("FF*FF****")).isDisjoint());
    assertTrue((new IntersectionMatrix("FF1FF2T*0")).isDisjoint());
    assertTrue(! (new IntersectionMatrix("*F*FF****")).isDisjoint());
  }

  public void testIsTouches() {
    assertTrue((new IntersectionMatrix("FT*******")).isTouches(P,A));
    assertTrue((new IntersectionMatrix("FT*******")).isTouches(A,P));
    assertTrue(! (new IntersectionMatrix("FT*******")).isTouches(P,P));
  }

  public void testIsIntersects() {
    assertTrue(! (new IntersectionMatrix("FF*FF****")).isIntersects());
    assertTrue(! (new IntersectionMatrix("FF1FF2T*0")).isIntersects());
    assertTrue((new IntersectionMatrix("*F*FF****")).isIntersects());
  }

  public void testIsCrosses() {
    assertTrue((new IntersectionMatrix("TFTFFFFFF")).isCrosses(P,L));
    assertTrue(! (new IntersectionMatrix("TFTFFFFFF")).isCrosses(L,P));
    assertTrue(! (new IntersectionMatrix("TFFFFFTFF")).isCrosses(P,L));
    assertTrue((new IntersectionMatrix("TFFFFFTFF")).isCrosses(L,P));
    assertTrue((new IntersectionMatrix("0FFFFFFFF")).isCrosses(L,L));
    assertTrue(! (new IntersectionMatrix("1FFFFFFFF")).isCrosses(L,L));
  }

  public void testIsWithin() {
    assertTrue((new IntersectionMatrix("T0F00F000")).isWithin());
    assertTrue(! (new IntersectionMatrix("T00000FF0")).isWithin());
  }

  public void testIsContains() {
    assertTrue(! (new IntersectionMatrix("T0F00F000")).isContains());
    assertTrue((new IntersectionMatrix("T00000FF0")).isContains());
  }

  public void testIsOverlaps() {
    assertTrue((new IntersectionMatrix("2*2***2**")).isOverlaps(P,P));
    assertTrue((new IntersectionMatrix("2*2***2**")).isOverlaps(A,A));
    assertTrue(! (new IntersectionMatrix("2*2***2**")).isOverlaps(P,A));
    assertTrue(! (new IntersectionMatrix("2*2***2**")).isOverlaps(L,L));
    assertTrue((new IntersectionMatrix("1*2***2**")).isOverlaps(L,L));

    assertTrue(! (new IntersectionMatrix("0FFFFFFF2")).isOverlaps(P,P));
    assertTrue(! (new IntersectionMatrix("1FFF0FFF2")).isOverlaps(L,L));
    assertTrue(! (new IntersectionMatrix("2FFF1FFF2")).isOverlaps(A,A));
  }

  public void testIsEquals() {
    assertTrue((new IntersectionMatrix("0FFFFFFF2")).isEquals(P,P));
    assertTrue((new IntersectionMatrix("1FFF0FFF2")).isEquals(L,L));
    assertTrue((new IntersectionMatrix("2FFF1FFF2")).isEquals(A,A));

    assertTrue(! (new IntersectionMatrix("0F0FFFFF2")).isEquals(P,P));
    assertTrue(  (new IntersectionMatrix("1FFF1FFF2")).isEquals(L,L));
    assertTrue(! (new IntersectionMatrix("2FFF1*FF2")).isEquals(A,A));

    assertTrue(! (new IntersectionMatrix("0FFFFFFF2")).isEquals(P,L));
    assertTrue(! (new IntersectionMatrix("1FFF0FFF2")).isEquals(L,A));
    assertTrue(! (new IntersectionMatrix("2FFF1FFF2")).isEquals(A,P));
  }

}
