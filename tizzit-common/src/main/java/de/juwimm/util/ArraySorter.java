/**
 * Copyright (c) 2009 Juwi MacMillan Group GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.juwimm.util;

/**
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public final class ArraySorter {
	private ArraySorter() {
	}
	
	/*
	 ** Sort in the same array
	 */
	public static void sort(Object[] a, Comparer comparer) {
		sort(a, null, 0, a.length - 1, true, comparer);
	}

	/*
	 ** Sort a and b, using a as the reference
	 */
	public static void sort(Object[] a, Object[] b, int from, int to, boolean ascending, Comparer comparer) {
		// No sort
		if (a == null || a.length < 2) { return; }
		// sort using Quicksort
		int i = from, j = to;
		Object center = a[(from + to) / 2];
		do {
			if (ascending) {
				while ((i < to) && (comparer.compare(center, a[i]) > 0)) {
					i++;
				}
				while ((j > from) && (comparer.compare(center, a[j]) < 0)) {
					j--;
				}
			} else {
				// Decending sort
				while ((i < to) && (comparer.compare(center, a[i]) < 0)) {
					i++;
				}
				while ((j > from) && (comparer.compare(center, a[j]) > 0)) {
					j--;
				}
			}
			if (i < j) {
				// Swap elements
				Object temp = a[i];
				a[i] = a[j];
				a[j] = temp;
				// Swap in b array if needed
				if (b != null) {
					temp = b[i];
					b[i] = b[j];
					b[j] = temp;
				}
			}
			if (i <= j) {
				i++;
				j--;
			}
		} while (i <= j);
		// Sort the rest
		if (from < j) {
			sort(a, b, from, j, ascending, comparer);
		}
		if (i < to) {
			sort(a, b, i, to, ascending, comparer);
		}
	}
}