///[SinErrores]
// Prueba un bloque con una asignacion, un atributo y un constructor

class Prueba1{

    int x = 4;
    float y = 3.e10;
    String str;
    private AnotherClass c = new Object();
    
    private Prueba1(int x) {
    	this.x = x;
	c = new AnotherClass();
    }

    static  void prueba1(int a) 
    {
        this.b().a = 5; 
	ClaseEstatica.llamado().concatenacion().llamados();
    
    }

    public Prueba1(int y){

    }
    
}

