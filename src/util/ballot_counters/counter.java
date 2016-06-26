package util.ballot_counters;

import java.io.*;
import java.util.*;

import util.Pair;

public class counter {
	public static iCountingSystem[] systems = new iCountingSystem[]{
		new CS_NTV_AnalogAllocation(),
		new CS_NTV_DigitalAllocation(),
		new CS_NTV_Approval(),
		new CS_NTV_NVotes(),
		
		new CS_MTV_AnalogAllocation(),
		new CS_MTV_DigitalAllocation(),
		new CS_MTV_Approval(),
		new CS_MTV_NVotes(),
		
		new CS_STV_Common(),
		new CS_STV_Correct(),
	};
	
	public static void main(String[] args) {
		Vector<String[]> v = readDelimited(new File("/Users/jimbrill/Documents/scenario1.txt"), "\t", "\r");
		for( int i = 0; i < v.size(); i++) {
			String[] ss = v.get(i);
			for( String s : ss) {
				System.out.print("["+s+"] ");
			}
			System.out.println();
		}
		Vector<MultiBallot> all_multi_ballots = new counter().getMultiBallots(v, 1);
		
		Vector<MultiBallot> multi_ballots = new Vector<MultiBallot>();
		multi_ballots.add(all_multi_ballots.get(0));
		
		System.out.print(all_multi_ballots.get(0).toString());
		
		for(int i = 0; i < systems.length; i++) {
			System.out.print(systems[i].getName()+": ");
			int[] winners = systems[i].getWinners( multi_ballots, 1);
			for( int j = 0; j < winners.length; j++) {
				System.out.print(winners[j]+",");
			}
			System.out.println();
		}
		
	}
	public static Vector<double[]> addNoise(double[] dd, int samples, int ballots) {
		double inc = 1.0/(double)samples;
		Vector<double[]> vdd = new Vector<double[]>();
		for(int i = 0; i < ballots; i++) {
			double[] de = new double[dd.length];
			for(int j = 0; j < samples; j++) {
				double r = Math.random();
				for(int k = 0; k < dd.length; k++) {
					r -= dd[k];
					if( r < 0) {
						de[k] += inc;
						break;
					}
				}		
			}
			vdd.add(de);
		}
		return vdd;
	}

	
	public Vector<MultiBallot> getMultiBallots(Vector<String[]> vs,int clones_per_party) {
		Vector<MultiBallot> mbs = new Vector<MultiBallot>();
		for( int i = 0; i < vs.size(); i++) {
			String[] ss = vs.get(i);
			double[] dd = new double[ss.length*clones_per_party];
			for( int j = 0; j < ss.length; j++) {
				double d = Double.parseDouble(ss[j].trim());
				for( int k = 0; k < clones_per_party; k++) {
					dd[j*clones_per_party+k] = d/(double)clones_per_party;
				}
			}
			mbs.add(new MultiBallot(dd));
		}
		return mbs;
	}
		
	
	public static Vector<String[]> readDelimited(File f, String cell, String line) {
		StringBuffer sb = new StringBuffer(); 
		Vector<String[]> v = new Vector<String[]>();
		try {
			FileInputStream fis = new FileInputStream(f);
			while( fis.available() > 0) {
				byte[] bb = new byte[fis.available()];
				fis.read(bb);
				sb.append( new String(bb));
				Thread.sleep(10);
			}
			fis.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		String s = sb.toString();
		String[] sss = s.split(line);
		for( int i = 0; i < sss.length; i++) {
			String[] ss = sss[i].split(cell);
			v.add(ss);
		}
		
		return v;
	}

}
