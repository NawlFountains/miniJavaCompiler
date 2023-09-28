///[SinErrores]
// Prueba un bloque con una asignacion, un atributo y un constructor

class Prueba1{

    int x = 4;
    float y,y2,y3 = 3.e10;
    boolean bol = 3 < ( 4 + 4 ) ;
    String str;
    String prt = variableClass.methodSomewhere(param1,param2,param3);
    private AnotherClass<Generics> c = new Object<GenericClass1,GenericClass2>();
    
    private Prueba1(int x) {
    	this.x = x;
	c = new AnotherClass();
    }

    static  void prueba1(int a) 
    {
	float x,y = 3.e1;
	CustomClass cusC,cusD = new CustomClass<GP>();
	CustomClass d = new CustomClass<>();
        this.b().a = 5; 
	ClaseEstatica.llamado().concatenacion().llamados();
    
	if ( Static.method() ) {
	} else {
		x.method().nested(parametre);
	}
	while( true ) {
		this.execute();
	}

    }
    void genericArgForMethod (C<Generic> c) {}

    public Prueba1(int y){

    }
    private static int testMethod() {
    	return x;
	return ;
    }
    public Class<Generic> testClassMethod() {}
    
}

