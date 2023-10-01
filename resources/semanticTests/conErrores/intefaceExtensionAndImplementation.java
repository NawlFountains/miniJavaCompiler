///[Error:|13]
// 
interface A{
	int methodA(int x);
}
interface B extends A{
	float methodB(int x);
}
class C implements B{
	float methodB(int z) {} 
	static void main(){}
}
