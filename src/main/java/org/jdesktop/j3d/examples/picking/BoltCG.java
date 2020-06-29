/*
 * $RCSfile$
 *
 * Copyright (c) 2007 Sun Microsystems, Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistribution of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL
 * NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF
 * USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR
 * ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL,
 * CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND
 * REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR
 * INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 *
 * $Revision$
 * $Date$
 * $State$
 */

package org.jdesktop.j3d.examples.picking;

import org.jogamp.java3d.CompressedGeometry;
import org.jogamp.java3d.CompressedGeometryHeader;

class BoltCG extends CompressedGeometry {

    BoltCG() {
	super(cgHeader, cgData) ;
    }

    private static final byte cgData[] = {
	25,    0, -120,   16, -124,   64,   33,  -35, 
         0,   67,   60,   48, -121,   90,    1,    3, 
       116,  -62,   25,  105,  -60,   60,  -32,    8, 
         5,  -58,   16,   -9, -114,   32,   -1, -104, 
        67,   16,    0, -117, -128,   97,   40,   62, 
       -62, -128, -122,    5,   67,   48,   10,  -76, 
       -32,   21,    1,    6,   40,   10, -128,   86, 
      -123,   24,  -96,   76,   65,   74,   88,    2, 
      -117,  -80,  -59,   21,  113, -118,  -40,  -28, 
        21,  110,    6,   40,   27,   64,   81,   23, 
      -124,   -7,   47,   54,   13,   -3,   -4,   69, 
        40,   25,  -69,  -99, -123,   64,    8,   48, 
         3,   64,   16,   23,   16,   97,  -39,    8, 
       -20, -125,    0,   36,    2,    1, -123,    2, 
         8, -120,   48,   27,  122,   91,   65,  -67, 
       108,   18,   26,   13,  -12,  -35,   95,  -48, 
       107,   63,   -5,   91,  -46,   12,   84,  -44, 
       -53, -120,   54,  -45,  -98,  115,   64,  -69, 
        92,  126,  -55,    1,    6,  -80,   34,  -49, 
       -24,   53,  -30,   61,  -19,    9,    6,  -76, 
        10, -115,  -24,   53,   87,  -54,   -2, -127, 
       113,   68,  -15,  -33,   16,   97,   -9,  113, 
       -54,  -30,   12,   75,   34,  -48,  -48,  107, 
        86,  -32,  -10, -125,   97,  -42,   19,  -78, 
        12,    0,  -48,    4,    4,   68,    8,   12, 
       -67,  -69,   32,  -64,    9,    0,    0,   92, 
        65, -121,  116,   42,   10,   13,   98,  -52, 
       -97,   16,   34,  104,  114,   75,   66,   12, 
       -45,   11,  -82,   32,  -34,  -74, -127,  -15, 
         6,   74,   51,   70,  -81,   32,  -42,   27, 
       105,   13,    6,  -97,   13,  111,  -24,   22, 
       -12,   22,  -19,  -95,    6,  -49,  -85,  -48, 
        16,  107,    2,   44,   -8, -125,    6,   73, 
      -123,  -97,   16,   37,   40,   68,  -22,  -30, 
        12,   58,   97,   80, -112,  104,  -16, -104, 
       -72, -125,  122,  -38,   22,  -12,   25, -114, 
       -39,  124,   64, -111, -115, -112,  -49,  -24, 
        53,  -34,   85,  -23,    5,    6,  -82,  -26, 
        15,  104,   54,    1,  -31,   62,   32,   68, 
       -17,  100,  -58,   84,   27,   33,  -54,  -66, 
      -125,   55,   -2,  -89,   32, -119,  122,  -73, 
       -25,  -36,  105, -128,    8,   53, -128,    2, 
        76,  -71,    9,  -56, -128,   30,  112,  -59, 
       -48,   96,  -37,    0,    0,    0,    9, -112, 
        28, -117,  100,   64,  127,   58,  113,  -58, 
        13,   96,    0, -109,   46,   70,   50,   80, 
       103,  -88,   56,  -86,   13,   96,    0, -109, 
        46,   67,  114, -112,   95,  -74,   49,   84, 
        49,  -74,  -72,   15,   96,    2,   80,    6, 
        55,   80,    0,  -36,   82,    0,   40,    3, 
       108,    1,  -58,  -95,  -63,  -84,    0,   18, 
       101,  -56,  -74,   87,   10,   56,   39,   31, 
       -32,  -43,  -32,    9,   50,  -28,   87,   46, 
      -125,   28,  -61, -117,  -48,  107,    0,    4, 
      -103,  114,   19, -105,  -65,  -98,  117,  -60, 
        32,  -63,  -84,    0,   18,  101, -116,  -74, 
        45,    1,  -15,    1,  -57,    0,  -29, -116, 
        26,  -64,    1,   38,   92, -116,  101,  111, 
        79,  124,  113,   83,   -6,  -68,    8,   37, 
       -36,  -83,   -4,  -93,   -4,  109,   46,   55, 
         6,  -81,    0,   73, -105,   33,  -71,   63, 
       -57,  -40,  -36,   82,   28,   26,  -64,    1, 
        38,   92, -117,  100, -113,   71,   71,  113, 
        -2,   13,   96,    0, -109,   46,   69,  114, 
        47,  -41,  -99,  -72,  -67,    6,  -80,    0, 
        73, -107,  -63,   72,  -64, -113,  -83,   99, 
        72,   53,   97,   -6,   65,   57,  -67,   60, 
       -38,  -26,  -63,  -78,  105,   11,  112,   85, 
       110,  101,  -95,   58, -128,   16,  107, -125, 
        90,  -60,    3,    0,    0,    1,    0,   32, 
        64,   67,  -32,  -54,  -41, -103,  113,    6, 
       -55,  -92,   45,  -63,   85,  -71, -106,  -34, 
       -22,    0,   55,  -79,  -36,  -28,   62,  -95, 
       -68,   97,  -73,  -41,  -70, -122,  -13,  117, 
       -50,   94,  -22,   32,   80,   21,   77,  -39, 
         6,    0,   48,    6,    2,   10,   13,   61, 
       106, -104,   80,   32,  -68, -125,   64,  -75, 
       100,   17,   91,  -78,   90,  -67,  -42,   24, 
        55,   27,  -39,   61,    0,  -56,   63,  -40, 
       -71,  -95, -124,   49,   74,   37,    3,    7, 
       -83,  118,  -18,   13,  -79,   30,  -96, -126, 
        80,   77,   53,   19,  -32,  -47, -121, -103, 
       104, -122,   82,   89,  -19,   56,   92,   45, 
       -96,  -73,   42, -128,   16,  109, -120,  -11, 
         4,   18, -126,  105,  -88,  126,   70, -127, 
       -56,  -24,   48,  -43,  -36,   20,  -25,  -48, 
       -60, -115,   -4,  -98,  -42,  -31,  -84,    8, 
       -35,  -13,   59,  -96,  -36,   11,    1,   64, 
        69,   65, -126,   80,  -85,   10,    4,   22, 
       -48,   97,  109,  -42, -114,   71,  112, -106, 
       -58,  -51,    6,  104,   48,   82,    8,  -19, 
       -74,  -36,  -81, -100,  -56, -122,   85,   34, 
      -111,  -37,  125,  -83,  103,   65,  -89,  125, 
       -30,  -15,   36,  -20,   -5,   81,  -63,   68, 
        27,  -17,  -89,  -83,    3,  -63,  -89,   33, 
       -24,   66,    0,    7,   50,   45,    2,  -56, 
        17,    6,  -99,   -9, -117,  -60, -109,  -77, 
       -19,  122,    5,   14,   96,  110,  114,   71, 
       -92,  117,   39,  107,   93,  -50,  105,   49, 
       110,  -75,   80,   46, -125,  -68,  -18, -125, 
        80,   60,   10,    1, -111,    6,  -76,   10, 
        48,  -96,   65,  105,    6, -120,   27,    8, 
        68,  -77, -109,  -59,   90,  -64,   16,  106, 
       -61,  -12,  -26,   17,   26,   10,   54,  -53, 
        40,  -88,    1,    6,  -14,  124,   52,   32, 
       -39,   13, -117,   66,   13,   63, -111,  -80, 
       -96,   65,  117,    6,  103,  -11, -120,  104, 
        54,  -49,  116,   92,   65, -102,  -84, -125, 
        50,   12,   87, -109,   67,   64, -124,  106, 
       -22,  -22,   12,   28,  -24,  -48,  -48,  109, 
        94,  -22,  -48, -125,   10,  -96,   75, -120, 
        17,   39, -119,  104,   65,  -92, -110,   68, 
        36,   27,   99,  -87,  -73,  -96,  -56,  -42, 
       -85,   18,   12,    8,    3,  -22,    0,   28, 
         1,  120,   34, -112,   -6,  -81,   52,  -64, 
         4,   24,  -88,    0,    0,   14,    0, -104, 
        39,   32,   44,   23,  -98, -125,    6,   44, 
         0,    0,    3, -128,   38,   22,  -56,   95, 
        13,  -25,   24,   49,   80,    0,    0,   28, 
         1,   48,  -58,  -28,  -38, -113,   42, -125, 
        21,    0,    0,    1,  -80,   19,   70,  -24, 
        73,  -46,  -13,   80,  -32,  -59,   64,    0, 
         0,  108,    7,  -94,  -37, -127,   -1,   30, 
       127, -125,   21,    0,    0,    1,  -64,   30, 
      -118,  -18, -124, -124,  121,  122,   12,   84, 
         0,    0,    7,    0,   78,   19,  -81,  -32, 
        99,  -56,   65, -125,   22,    0,    0,    1, 
       -64,   30,   11,  110,   64,  117,    0,    0, 
         2, -106,   48,  106,    2,   56, -102,   99, 
       126,   15,   53,   37,   65,  -81,   -8,  -30, 
       104,  -36,  -75,   -8,  -98,   82,   28,   24, 
       -88,    0,    0,   13, -128, -104,   91,   93, 
       -20,  -41,  -97,  -32,  -59,   64,    0,    0, 
       112,    7,  -94,  -69,   31,   56,  -98,   94, 
      -125,   21,    0,    0,    1,  -64,   24,   77, 
       -96,  -39,  -12,   65, -125,   41,   86,  -79, 
        58,   14,  -56,   87,  102, -112,  105,  -50, 
        18,  -42,   11,   91,   10,  -54,   -9,    6, 
        61,   -1,  -87,    3,   51,   55,  -14,   86, 
       108,   80,  111,   -5,  -18, -110,   55,   34, 
       112,  -43,  114, -123,    6,    1,   63,    9, 
        32,  -11,   21,   28,   87,   36,    4,   32, 
       -56,   83,   -3,   36,  -82,   -1,  -60,  -86, 
       -31,  -63,  -88,  -40,   98,   73,  -99,  -72, 
        71,   27,   59,    6,    3,   38, -110,   12, 
       111,  -92,    4,  -39,   -8,   49, -111,   74, 
      -112,   51,   28,  -18,  -90,  -64,   37,    6, 
        80,  -54,  -38,  -51,  106,  -93,   73,  -80, 
         5,    8,   50,  119,   84, -116,   47,  110, 
       123,  -10,  -60,   45,    6,   45,  104,   81, 
      -116,  106,   15,  126,  -40,  -40,   48,  101, 
        42,  -30,  109,   90,  -16,   69,  123,  -81, 
       -47,    6,  112,    0,    0,    9,    1,    4, 
        78,    1,   17, -114,  -67,    6,   12,  -32, 
         0,    0,   18,    2,    9,  108, -111,  -28, 
        29,  113, -125,   56,    0,    0,    4, -128, 
      -124,   99,  119,   92,   87,   85,   65, -100, 
         0,    0,    2,   64,  106,   53, -112,   31, 
       101,  -80,   30,   13,  -35,  -80,   85,  -86, 
       -95,  -63, -109,   -1,  117,  -46,  -38,   46, 
       -94,  -70,  127,    6, -112,    0,    0,  119, 
         1,  124,  -82,   39,   15,   46, -113,   65, 
       -92,    0,    0,   29, -128,  127,   19,  -67, 
        -4, -112,  -82, -127,    6,   13,   40,    0, 
         0,  -18,    2,  -15,  108,  -81,  -40,   29, 
        49, -125,   72,    0,    0,   59,    0,  -70, 
        99,    9,  -61, -106,  -91,   65, -112,   31, 
       102,  -44,   27,   62, -118,  116,   65,  -92, 
         0,    0,   29,  -64,  117,   65, -100,    0, 
         0,    2,   64,  122,   27, -111, -127,  -35, 
       -75,   33,  -63,  -80,    8,  -92,   34,  -40, 
       -52,   86,  -70,   -1,    6,  108,    0,    0, 
        10,    1,    4,  -82,  122,  114, -114,  -81, 
        65, -100,    0,    0,    2,   64,  115,   26, 
      -113, -107,  -98, -115,  -46,  -95,  -63,  -71, 
         0,    0,   28, -128,   87,   65, -123,  -57, 
       -69,  -22,   12,    4,    9,  -32,  118,   65, 
      -128,   24,    7, -128,  -96, -127,   27, -128, 
       101,  -12,   27,   36,   13,  -93,  -96,  -50, 
       -29,   89,  -31,    6, -114,    0,    0,   29, 
       -96,   48,  -96,   65,   -7,    6,   -4,   72, 
        30,  -79,   32,  -56,   32,   59,  -32,   60, 
        96,   23,  -48,  110, -124,   50,  -80, -127, 
       116, -126,  -89,  100,   25, -128,    0,  120, 
        15,   72,   51,  119,  -74,   52,   32,  -38, 
        -8,  -94,  -70,    5,   65, -123,   30,   16, 
       104,  -96,    0,    1,  -36,    3,   42,   13, 
        -1,  127,  124,   26, -122,  -64,    5,  -22, 
        -4,   74,   12,    0,   68, -102,  -48,   99, 
       117,    1,   73,    6,  -72,  -55,  -17, -120, 
        54,  115,  -83,   -3,   73,    6,  -46,   15, 
       -85,  -88,   50,  112,  -91,  104,   65, -110, 
        43,   22,  -12,   27,   27,   83,  104,   65, 
      -114, -126, -105,  -64, -119,  -33,  105, -120, 
       -71,   76,    0,   65,  -81,  -99,  -54,   82, 
        13,    3,   78,   81,   80,  107,  -25,   76, 
      -122, -125,    0,  -60,   23,  -28,   10,  -17, 
       -97,   42,  -24, -125,  112,    0,    0,   56, 
      -128,  -76, -125,   32,  -43,  -89,  -92,   27, 
       -65,   50,  -99, -112,   96,    1,    0,  -32, 
        40,  -96,   72,   49, -107,   57,    6,  -34, 
      -109,  104,   40,   51,   12,   68,   67,   64, 
      -128,   98,   11,  -78,   12,  -64,   16,   24, 
         7, -108,   25,  -64,    0,    0,    9, -128, 
       -16,  109,   72,    0,    0,   14,  -48,   30, 
       -20,   56,   55,  -23,    7,   43,  -96,  -54, 
       -29,  -35,   97,    6,  -88,   69,   13,    8, 
        49,   69,  -46,   -6, -127,   14,   68, -125, 
       -34,   80,  103,    0,    0,    0,   36,    2, 
        58,   12,  110,   53,  -44,   80,  110, -112, 
       112,  -54, -125,  127,  -33,  -40,   80,   32, 
       -68, -125,   59,  -18,  -74,   36,   25,    0, 
         0,    0,    7,  -66,    3,  -14,   13,   -1, 
       -12,   31,  104,   64, -118,   46, -105,  100, 
        24,    1,   32,  104,   14, -120,   55,   32, 
         0,    3, -112,   15,  -29,   85,   84, -127, 
         5, -120,    9,   65, -112,    0,    0,    0, 
       123,  -32,   63,   32,  -38,  -57,  127,   55, 
       -44,   24,  -97,   40,   18,   -4, -125,  105, 
        56,  -15,   95,   80,   96,  -68,   16,  -93, 
       122,    5,  110,  -88,   40,   32,  -64,   20, 
       113,  -11,    6,   11,  -59,    8,  -69,   32, 
       -64,   12,    0,  -64,  111,   64,  -83,  -43, 
         7,  100,   24, -128,  -96,   88,   12,  104, 
        53,   -4,    0,    0,    0, -124,    7,  -64, 
      -118,  -33, -106, -102,  -13,  -84,    0,   65, 
      -100,    0,    0,    0, -104,    9,   72,   50, 
        13,   56,  126,   65,  -70,   -4,   -6,  -49, 
       -88,   50,  112,  -39,  -59, -115,    6,  -83, 
         1,  -13,    1,  -53,    0,   -8, -125,   37, 
        13,   34,  -41,   16,  107,  -25,  116, -122, 
      -125,    0,  -60,    7,  -16, -118,  102,  102, 
       -36,  -66, -118,    0,   65,  -67,   -1,  -30, 
        10,   12,  -99,   74,   94,  -48,  110,  -31, 
       108,  104,   65, -102,  -87,  103,  -60,    8, 
        34, -100,   36,   -2, -125,  124,  -84,   -5, 
       -97,   16,   98,   43,  -46,   27,   34,   12, 
       127, -101,  127,   64,  -80,  -47,  -16,  -19, 
         8,   53,  -33,   88,   -8, -125,   20,  109, 
        19,   95,  -48,  111, -111,   -1, -117,   66, 
        13,  -57,   92,   63,  -96,   93, -104,  -99, 
       119,  -60,   24,   84,  -16,   82,   -8, -125, 
         3,   80,    2,  -38,   16,  103, -116,  101, 
       -19,    2,  -18,  110,  -26,   68,   24, -117, 
       -98,  -56, -125,    5,  -64,  -35, -112,   96, 
         4,    0,   32,   63, -102,   89,   15,  119, 
      -122,  112,   12,   32,  -38,  -68,   70, -110, 
        91, -114,  -68,  111,    2,   16,   96,  -65, 
       -23, -123,    2,   13,    7,  118,  -36,  104, 
      -127,   46,  -26, -117,  -92,  -19,   36,  102, 
         0, -128,  -64,   61,  -61,   84,   87,  -73, 
        74,   32,  -24,   50,   11,   10,  111,   57, 
       -79,  -36,  -28,   10,   93,   24,    4,   22, 
       106,  -32,  -42,   17,  -42,  -29, -104,  -37, 
        74,  105,  -73,   -9,  -48,  112,  -35,  -82, 
       107,  119,   73,   10,  -33,   78,   -4,  -79, 
       -97,   96,  -54,  -14,   54, -128,   58,   97, 
         8,  111,    4,    3,  -50,  -37,  -53,   64, 
      -124,    0,   14, -103,   65,  -41, -127,    8, 
        53,    6,  -14,   65,   65,  -97,  -48,  115, 
       113,  -51,  -74,   42,   50,   82,   57,   40, 
       -86,  -53, -100,  -41,  -66,  -74,  106, -125, 
        82,  116,  -39,  -48,   38,   51,  107,  121, 
        44,   88,  -28,   34,   96,    4,  -78,    4, 
        75,  118,  -64,   16,   96,    4, -127,  -96, 
        45, -110,   88,   14,  -31,    3,    0,   65, 
       -65, -128,   10,   25,   44,  -61,   81,  -40, 
       -16,    3,  -85, -100,    0,   20,    6,  -12, 
         3,   49,  -63, -107,   14,   15,   71,  -16, 
        99,   19,  -81,  -37, -102,  -15,  -37,   32, 
       -21,   -7,    6,  -91,  -81,   91, -119,   -4, 
        88,  -77,  -20,   26,  -49,  -96,  -54,   23, 
       112,  -72,  -41,  -35,   64,    8,   53,   11, 
        14,  102,   65, -123,  -35,  -52,  -94,   55, 
        24,   -7,  -45,  -72,  -75, -123,    0,    0, 
        22,  -64,  111,  -16,    2,   13,   97,  -46, 
        48,  -96,   65,    0,   86,   40,   32, -120, 
        21, -124, -115,   46,   54,   29,  -69,   11, 
        88,    0,  -96,   96,   11,  -32,   60,   20, 
       -94,   25,   99,   53,   97, -125,    8,   91, 
       -48,    2,  101,  113,  -54, -128,  -20,    0, 
        10,  -85,  -70, -117,   50,   32,    1,    6, 
        75,   84,   46,  -99,  -38,  -53,  -51,    9, 
       -59,  -59,   52,   25,  121,   16,    0,  119, 
        27, -114, -108,   39,   21,    4, -111,  -26, 
       116,   64,    2,   13,  121, -104,  105,   63, 
      -115,   58,  -42,  122, -125,  106,  -15,   25, 
      -112,   99,   35,  -32,    8,    5,  -96,    0
    } ;

    private static final CompressedGeometryHeader cgHeader ;

    static {
	cgHeader = new CompressedGeometryHeader() ;
	cgHeader.majorVersionNumber = 1 ;
	cgHeader.minorVersionNumber = 0 ;
	cgHeader.minorMinorVersionNumber = 1 ;
	cgHeader.bufferType = CompressedGeometryHeader.TRIANGLE_BUFFER ;
	cgHeader.bufferDataPresent = CompressedGeometryHeader.NORMAL_IN_BUFFER ;
	cgHeader.start = 0 ;
	cgHeader.size = cgData.length ;
    }
}
