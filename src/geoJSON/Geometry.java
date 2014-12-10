package geoJSON;

import serialization.JSONObject;
import serialization.ReflectionJSONObject;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.*;

public class Geometry extends ReflectionJSONObject<Geometry> {
	public String type;
	public double[][][] coordinates;
	public int[] xpolys;
	public int[] ypolys;
	public Polygon[] polygons;
	public Color c = Color.BLACK;
	public boolean isDistrict = true;
	
	public static boolean isLatLon = false; 
	
	public static double shiftx,shifty,scalex,scaley;
	public void makePolys() {
		polygons = new Polygon[coordinates.length];
		for( int i = 0; i < coordinates.length; i++) {
			xpolys = new int[coordinates[i].length];
			ypolys = new int[coordinates[i].length];
			for( int j = 0; j < coordinates[i].length; j++) {
				xpolys[j] = (int)((coordinates[i][j][0]-shiftx)*scalex);
			}
			for( int j = 0; j < coordinates[i].length; j++) {
				ypolys[j] = (int)((coordinates[i][j][1]-shifty)*scaley);
			}
			polygons[i] = new Polygon(xpolys, ypolys, xpolys.length);
		}

	}
	public Geometry() {
		super();
		int r = (int)Math.floor(Math.random()*256.0);
		int g = (int)Math.floor(Math.random()*256.0);
		int b = (int)Math.floor(Math.random()*256.0);
		c = new Color(r,g,b);
	}

	@Override
	public void post_deserialize() {
		super.post_deserialize();
		
		if( containsKey("coordinates")) {
			Vector<Vector<Vector<Object>>> vvvo = (Vector<Vector<Vector<Object>>>)getVector("coordinates");
			coordinates = new double[vvvo.size()][][];
			int i2 = 0;
			for( int i = 0; i < vvvo.size(); i++) {
				try {
					Vector<Vector<Object>> vvo = vvvo.get(i);
					coordinates[i2] = new double[vvo.size()][];
					int k2;
					k2 = 0;
					for( int k = 0; k < vvo.size(); k++) {
						try {
							Vector<Object> vo = vvo.get(k);

							coordinates[i2][k2] = new double[]{
									Double.parseDouble((String)vo.get(0)),	
									Double.parseDouble((String)vo.get(1)),
							};
						k2++;
						} catch (Exception ex) { 
							//System.out.println("ex1 "+vvo.get(k));
						}
					}
					double[][] dd = new double[k2][];
					for( int k = 0; k < dd.length; k++) {
						dd[k] = coordinates[i2][k];
					}
					coordinates[i2] = dd;
					i2++;
				} catch (Exception ex) { 
					//System.out.println("ex "+vvvo.get(i));
					//ex.printStackTrace();
				}
			}
			double[][][] dd = new double[i2][][];
			for( int i = 0; i < dd.length; i++) {
				dd[i] = coordinates[i];
			}
			coordinates = dd;
			this.remove("coordinates");
			
		}
		
		// TODO Auto-generated method stub
		
	}
	public double[] compute2DPolygonCentroid(Polygon p) {
		return compute2DPolygonCentroid(p.xpoints,p.ypoints);
	
	}
	public double[] compute2DPolygonCentroid(int[] xs, int[] ys) {
			    double signedArea = 0.0;
			    double x0 = 0.0; // Current vertex X
			    double y0 = 0.0; // Current vertex Y
			    double x1 = 0.0; // Next vertex X
			    double y1 = 0.0; // Next vertex Y
			    double a = 0.0;  // Partial signed area

			    double retx = 0;
			    double rety = 0;
			    for(int i=0; i < xs.length; i++)
			    {
			        x0 = xs[i];
			        y0 = ys[i];
			        x1 = xs[i+1 == xs.length ? 0 : i+1];
			        y1 = ys[i+1 == xs.length ? 0 : i+1];
			        a = x0*y1 - x1*y0;
			        signedArea += a;
			        retx += (x0 + x1)*a;
			        rety += (y0 + y1)*a;
			    }

			    signedArea *= 0.5;
			    retx /= (6.0*signedArea);
			    rety /= (6.0*signedArea);

			    return new double[]{retx,rety};
	}

	@Override
	public void pre_serialize() {
		super.pre_serialize();
		if( coordinates != null) {
			Vector<Vector<String>> v = new Vector<Vector<String>>();

			Vector v3 = new Vector();
			for( int j = 0; j < coordinates.length; j++) {
				for( int i = 0; i < coordinates.length; i++) {
					Vector v2 = new Vector();
					v2.add(""+coordinates[j][i][0]);
					v2.add(""+coordinates[j][i][1]);
					v.add(v2);
				}
				v3.add(v);
			}
			put("coordinates",v3);
		}
		// TODO Auto-generated method stub
		
	}

	@Override
	public JSONObject instantiateObject(String key) {
		// TODO Auto-generated method stub
		return super.instantiateObject(key);
	}
}