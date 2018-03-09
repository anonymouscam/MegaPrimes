//package com.fermatslast.MegaPrimes;

import java.util.Scanner;
public class Solution {
	
	/*
	 * Given a number n, returns the next closest number only consisting of valid digits 2,3,5,7
		@param long n (number to find closest number to)
		@return long (cloest number >= n, consisting of only digits 2,3,5,7)
	*/
	private static long getClosest( long n ) {
		int[] validDigits = new int[]{2,3,5,7};
		long result = 0L;
		long clearBefore = 1;
		
		boolean carry = false;
		long temp = n;
		long mult = 1;
		while ( temp > 0L ) {
			
			long digit = (temp % 10L);
			if ( carry ) {
				digit += 1;
				carry = false;
			}
			
			int j = 0;
			while ( j < validDigits.length ) {
				if ( digit < validDigits[j] ) {
					result += mult * validDigits[j];
					clearBefore = mult;
					break;
				}
				else if ( digit == validDigits[j] ){
					result += mult * validDigits[j];
					break;
				}
				else {
					j++;
				}
			}
			if ( j >= validDigits.length ) {
				clearBefore = mult * 10;
				carry = true;
			}
			
			mult *= 10L;
			temp /= 10L;
		}
		if ( carry ) {
			result += mult * validDigits[0]; 
			clearBefore = mult;
		}
		
		//zero out digit before clearBefore
		result -= (result % clearBefore);
		//replace them with smallest valid digit
		result += (clearBefore - 1L)/9 * validDigits[0];

		
		return result;
	}
	
	/*
	 * given a valid number consisting of only 2,3,5,7 returns the next number
	 * @param long n (number to get the next one from)
	 * @return long (next valid number > n consisting of only 2,3,5,7
	 */
	private static long getNext( long n ) {
		long result = 0L;
		
		long after = 0L;
		long temp = n;
		long mult = 1L;
		boolean carry = true;
		
		int[] nextDigit = new int[]{2,2,3,5,5,7,7,2,2,2};
		boolean[] mustCarry = new boolean[]{false,false,false,false,false,false,false,true,true,true};
		
		while ( temp > 0 && carry ) {
			int digit = (int) (temp % 10);
			int next = nextDigit[digit];
			carry = mustCarry[digit];
			after += mult * next;
			temp /= 10;
			mult *= 10;
		}
		if ( carry ) {
			after += mult * nextDigit[0];
		}
		result = (n/mult) * mult;
		result += after;
		
		//nums > 10 ending in 2 or 5 are not prime, so skip them 
		if ( result > 10 ) {
			long lastdigit = result % 10;
			if ( lastdigit == 2 ) {
				result += 1;
			}
			else if ( lastdigit == 5 ) {
				result += 2;
			}
		}
		
		return result;
	}
	
	private static boolean isPrime( long n ) {
		if ( n < 2 ) { return false; }
		if ( n == 2 ) { return true; }
		if ( n == 3 ) { return true; }
		if ( n % 2 == 0 ) { return false; }
		if ( n % 3 == 0 ) { return false; }
		long i = 5;
		while ( i * i <= n ) {
			//6k-1
			if ( n % i == 0 ) {
				return false;
			}
			i += 2;
			// 6k+1
			if ( n % i == 0 ) {
				return false;
			}
			//next 6k-1
			i += 4;
		}
		
		return true;
	}

	private static long numMegaPrimes(long start, long end) {
		long result = 0L;
		
		long closest = getClosest(start);
		long i = closest;
		while ( i <= end ) {
			if ( isPrime(i) ) {
				result++;
			}
			
			i = getNext(i);
		}
		
		
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
