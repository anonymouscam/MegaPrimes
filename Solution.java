package com.fermatslast.MegaPrimes;

import java.util.BitSet;
import java.util.Scanner;
public class Solution {
	
	public static final long maxSize = 1000000;
	
	private static boolean isValidNum( long n ) {
		boolean[] isValid = new boolean[]{false, false, true, true, false, true, false, true, false, false};
		if ( n == 0 ) {
			return false;
		}
		
		while (n > 0L ) {
			int digit = (int) (n % 10L);
			if ( !isValid[digit] ) {
				return false;
			}
			n /= 10;
		}
		
		return true;
	}
	
	private static long numValidPrimes( long sieveStart, long sieveEnd, BitSet rootsieve ) {
		if ( sieveStart > sieveEnd ) {
			return 0L;
		}
		
		long result = 0L;
		BitSet segsieve = getSegmentedSieve(rootsieve, sieveStart,sieveEnd );
		int i = -1;
		do {
			i = segsieve.nextSetBit(i+1);
			if ( i != -1) {
				if ( isValidNum( i + sieveStart) ) {
					//System.out.println( "validPrime: " + (i + sieveStart));
					result++;
				}
			}
		} while (i != -1 ); 
		
		return result;
		
	}
	
	private static long numMegaPrimesR( long prefix, long mult, long start, long end, BitSet rootsieve ) {
		
		if ( prefix * mult > end) {
			return 0L;
		}
		if ( ((prefix + 1) * mult - 1) < start ) {
			return 0L;
		}
		
		if ( mult <= maxSize ) {
			//calculate for block
			long sieveStart = Math.max( start, prefix * mult );
			long sieveEnd = Math.min( end, (prefix + 1) * mult - 1 );
			
			return numValidPrimes( sieveStart, sieveEnd, rootsieve );
		}
		
		long result = 0L;
		long nextMult = mult/10L;

		if ( prefix == 0L ) {
			result += numMegaPrimesR( prefix * mult + 0, nextMult, start, end, rootsieve );
		}
		result += numMegaPrimesR( prefix * 10 + 2, nextMult, start, end, rootsieve  );
		result += numMegaPrimesR( prefix * 10 + 3, nextMult, start, end, rootsieve  );
		result += numMegaPrimesR( prefix * 10 + 5, nextMult, start, end, rootsieve  );
		result += numMegaPrimesR( prefix * 10 + 7, nextMult, start, end, rootsieve  );
		
		return result;
		
	}
	
	private static BitSet getSegmentedSieve( BitSet rootsieve, long start, long end ) {
		
		//System.out.printf("segSieve: start: %d end: %d\n", start, end);
		
		int n = (int) (end-start+1);
		BitSet segsieve = new BitSet(n);
		segsieve.set(0,n);
		
		//if segsieve includes numbers < 2, we must clear them
		for ( long j = start; j < end && j < 2; j++ ) {
			segsieve.clear((int) (j - start) );
		}
		
		
		
		int i = 1;
		while ( true ) {
			i = rootsieve.nextSetBit(i+1);
			if ( i == -1 || i > end ) {
				break;
			}
			
			long first = (long) Math.ceil( (double)start/(double)i ) * i;
			if ( first == i ) {
				first += i;
			}
			long last = (end/i) * i;
			
			//System.out.printf("prime: %d first: %d last: %d\n", i, first, last);
			for ( long j = first; j <= last; j += i ) {
				segsieve.clear((int) (j - start) );
			}
			
		}
		
		return segsieve;
	}
	
	private static BitSet getSieve( int n ) {
		BitSet sieve = new BitSet((int) n+1);
		sieve.set(0,n+1);
		sieve.clear(0,2);
		
		for ( int i = 2; i <= n; i++ ) {
			if (sieve.get(i) ) {
				//i is prime
				
				//clear multiples
				for ( int j = i+i; j <= n; j += i ) {
					sieve.clear(j);
				}
				
			}
		}
		
		
		return sieve;
	}
	
	private static long numMegaPrimes(long start, long end) {
		long result = 0L;
		
		//divide into smaller chunks
		long mult = 1L;
		while ( mult < end ) {
			mult *= 10L;
		}
		mult /= 10L;
		
		int sqrtn = (int) Math.ceil( Math.sqrt( end ) );
		BitSet rootsieve = getSieve( sqrtn );
		
		result += numMegaPrimesR( 0, mult, start, end, rootsieve );
		result += numMegaPrimesR( 2, mult, start, end, rootsieve );
		result += numMegaPrimesR( 3, mult, start, end, rootsieve );
		result += numMegaPrimesR( 5, mult, start, end, rootsieve );
		result += numMegaPrimesR( 7, mult, start, end, rootsieve );
		
		return result;
	}
	
	public static void main(String[] args) {
		Scanner stdin = new Scanner(System.in);
		long p = stdin.nextLong();
		long q = stdin.nextLong();
		
		System.out.println( numMegaPrimes(p,q) );
		
		stdin.close();
	}



}
