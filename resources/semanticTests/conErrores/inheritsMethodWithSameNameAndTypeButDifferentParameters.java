///[Error:method|10]
// 
class A{
	int method(int x) {}
	static void main(){}
}
class B extends A{
}
class C extends B{
	int method() {}
}
