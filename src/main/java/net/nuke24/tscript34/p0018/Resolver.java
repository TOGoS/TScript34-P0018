package net.nuke24.tscript34.p0018;

interface Resolver<T> {
	/** May return null to indicate 'idk' */
	T get(String name);
}
