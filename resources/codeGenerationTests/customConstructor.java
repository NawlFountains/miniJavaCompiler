///90&exitosamente

class A{
    int x;
    
	public A(int a) {
		x = a;
	}   
}


class Init{
    static void main()
    { 
        var a = new A(90);
        debugPrint(a.x);
        
    }
}


