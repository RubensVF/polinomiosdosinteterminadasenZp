

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class n5polinomios{

    public static void main(String[] args){
        
        System.out.println(args[0]);
        /*int a[]= new int[1000];
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String  lines = br.readLine();    
        
        String[] strs = lines.trim().split("\\s+");
            
        for (int i = 0; i < strs.length; i++) {
        a[i] = Integer.parseInt(strs[i]);
        }
        
        int p  = a[0];
        int n1 = a[1];
        int n2 = a[2];
        
        Polinomio p1 = new Polinomio(p);
        Polinomio p2 = new Polinomio(p);
        
        for(int i=3;i<n1*3+2;i+=3){
            System.out.println("c1="+a[i]);
        
            Zp coef  = new Zp(a[i],p);
            int expx = a[i+1];
            System.out.println("expx1="+a[i+1]);
            int expy = a[i+2];
             System.out.println("expy1="+a[i+2]);
            p1.inserta(coef, expx, expy);
            
        }
        
        for(int i= 3+n1*3;i<n2*3+3+n1*3;i+=3){
            Zp coef  = new Zp(a[i],p);
            int expx = a[i+1];
            int expy = a[i+2];
            p2.inserta(coef, expx, expy);
        }
        p1.escribepolinomio();
        p2.escribepolinomio();
        p1.suma(p2).escribepolinomio();
        p1.resta(p2).escribepolinomio();
        p1.producto(p2).escribepolinomio();*/
    }
}

class Polinomio {

    private Monomio principal;
    private int p;

    public Polinomio(int p) {
        principal = null;
        this.p = p;
    }

    public Polinomio(Monomio P) {
        principal = P;
        this.p = P.obtencoeficiente().obtenP();
    }

    private Monomio obtenprincipal() {
        return principal;
    }

    public void inserta(Zp coeficiente, int exponentex, int exponentey) {
        if (coeficiente.escero()) return;
        Monomio aux = new Monomio(coeficiente, exponentex, exponentey);
        if (principal == null) {
            principal = aux;
            return;
        }
        if (principal.grado() < aux.grado()) {
            aux.apuntasig(principal);
            principal = aux;
            return;
        }
        if (principal.grado() == aux.grado()) {
            if (principal.obtenexponentex() == exponentex) {
                if (principal.obtencoeficiente().suma(coeficiente).escero()) {
                    principal = principal.obtensig();
                    return;
                } else {
                    principal.sumacoeficiente(coeficiente);
                    return;
                }
            }
            if (principal.obtenexponentex() < exponentex) {
                aux.apuntasig(principal);
                principal = aux;
                return;
            } else {
                Monomio paux = principal;
                while (paux.obtensig() != null) {
                    if (aux.obtenexponentex() > paux.obtensig().obtenexponentex()) {
                        aux.apuntasig(paux.obtensig());
                        paux.apuntasig(aux);
                        return;
                    } else {
                        paux = paux.obtensig();
                    }
                }

            }
        }

        Monomio paux = principal;
        while (paux.obtensig() != null) {
            if (aux.grado() > paux.obtensig().grado()) {
                aux.apuntasig(paux.obtensig());
                paux.apuntasig(aux);
                return;
            }
            if (aux.grado() == paux.obtensig().grado()) {
                if (aux.obtenexponentex() == paux.obtensig().obtenexponentex()) {
                    if (paux.obtensig().obtencoeficiente().suma(coeficiente).escero()) {
                        paux.apuntasig(paux.obtensig().obtensig());
                        return;
                    } else {
                        paux.obtensig().sumacoeficiente(coeficiente);
                        return;
                    }
                } else {
                    Monomio paux2 = paux.obtensig();
                    while (paux2.obtensig() != null) {
                        if (aux.obtenexponentex() > paux2.obtensig().obtenexponentex()) {
                            aux.apuntasig(paux2.obtensig());
                            paux2.apuntasig(aux);
                            return;
                        } else {
                            paux2 = paux2.obtensig();
                        }
                    }
                }
            }
            paux = paux.obtensig();
        }
        paux.apuntasig(aux);
    }

    public void escribepolinomio() {
        if (principal == null) {
            System.out.println("0(x,y)");
            return;
        }
        Monomio paux = principal;
        while (paux != null) {
            System.out.print("+");
            int expx = paux.obtenexponentex();
            int expy = paux.obtenexponentey();
            paux.obtencoeficiente().escribeZp();

            if (expx == 1) {
                System.out.print("x");
            }
            if (expx > 1) {
                System.out.format("x^%d", paux.obtenexponentex());
            }
            if (expy == 1) {
                System.out.print("y");
            }
            if (expy > 1) {
                System.out.format("y^%d", paux.obtenexponentey());
            }
            
            paux = paux.obtensig();
        }
        System.out.println();
    }

    public Polinomio suma(Polinomio sumando) {
        Polinomio pol = new Polinomio(this.p);
        Monomio paux = this.principal;
        while (paux != null) {
            pol.inserta(paux.obtencoeficiente(), paux.obtenexponentex(), paux.obtenexponentey());
            paux = paux.obtensig();
        }
        paux = sumando.obtenprincipal();
        while (paux != null) {
            pol.inserta(paux.obtencoeficiente(), paux.obtenexponentex(), paux.obtenexponentey());
            paux = paux.obtensig();
        }
        return pol;
    }

    public Polinomio resta(Polinomio sumando) {
        Polinomio pol = new Polinomio(this.p);
        Monomio paux = principal;
        while (paux != null) {
            pol.inserta(paux.obtencoeficiente(), paux.obtenexponentex(), paux.obtenexponentey());
            paux = paux.obtensig();
        }
        paux = sumando.obtenprincipal();
        while (paux != null) {
            pol.inserta(paux.obtencoeficiente().inverso(), paux.obtenexponentex(), paux.obtenexponentey());
            paux = paux.obtensig();
        }
        return pol;
    }

    public Polinomio producto(Polinomio q){
            Polinomio pol=new Polinomio(this.p);
            if(principal==null )return pol;
            if(q.principal==null)return pol;
            Monomio aux1=principal;
            while(aux1!=null){
                Monomio aux2=q.obtenprincipal();
                while(aux2!=null){
                        Zp coef  = aux2.obtencoeficiente().producto(aux1.obtencoeficiente());
                        int expx = aux2.obtenexponentex()+aux1.obtenexponentex();
                        int expy = aux2.obtenexponentey()+aux1.obtenexponentey();
                        pol.inserta(coef,expx,expy);
                        aux2=aux2.obtensig();
                        }
                aux1=aux1.obtensig();
                }
            return pol;
            }
    /*
    public POLINOMIO cociente(POLINOMIO divisor,POLINOMIO resuido)
        {
        POLINOMIO cociente=new POLINOMIO();
        if(principal.obtenexponente()<divisor.obtenprincipal().obtenexponente())
            {
            System.out.println("Fatal error gr(f)<gr(g)");
            return cociente;
            }
        POLINOMIO aux=new POLINOMIO();
             aux.principal=principal;
        while(aux.obtenprincipal().obtenexponente()>=divisor.obtenprincipal().obtenexponente())
            {
            POLINOMIO caux=new POLINOMIO();
            cociente.inserta(aux.obtenprincipal().obtencoeficiente().divide(divisor.obtenprincipal().obtencoeficiente()), aux.obtenprincipal().obtenexponente()-divisor.obtenprincipal().obtenexponente());
            caux.inserta(aux.obtenprincipal().obtencoeficiente().divide(divisor.obtenprincipal().obtencoeficiente()), aux.obtenprincipal().obtenexponente()-divisor.obtenprincipal().obtenexponente());
            System.out.println();
            aux=aux.resta(caux.producto(divisor));
            aux.escribepolinomio();
            if(aux.obtenprincipal()==null)break;
            }
        resuido.principal=aux.principal;
        return cociente;
        }*/
}


class Monomio {
            
    private int exponentex;
    private int exponentey;
    private Zp coeficiente;
    private Monomio sig;
    public Monomio(Zp coeficiente, int exponentex,int exponentey)
        {
        this.exponentex =exponentex;
        this.exponentey =exponentey;
        this.coeficiente=coeficiente;
        this.sig=null;
        }
    public int obtenexponentex(){
                return exponentex; 
                }
    public int obtenexponentey(){
                return exponentey; 
                }
    public Zp obtencoeficiente(){
                return coeficiente; 
                }
    public void sumacoeficiente(Zp  sumando){
                 coeficiente=coeficiente.suma(sumando);
                }
    public Monomio obtensig(){
                return sig;
                }
    public void apuntasig(Monomio sig){
                this.sig=sig;
                }
    public int grado(){
        return exponentex+exponentey;
    }
   
}


class Zp {
    protected int z;
    protected int p;
    
    public Zp(int z,int p){
        this.p=p;
        this.z=z%p;
    }
        
    public int obtenZ(){
        return z;
    }
    public int obtenP(){
        return p;
    }
    
    public Zp suma(Zp sumando){
        int suma=obtenZ()+sumando.obtenZ();
        return new Zp(suma,p);
    }
    
    public Zp producto(Zp producto){
        int ret=obtenZ()*producto.obtenZ();
        return new Zp(ret,p);
    }
    
    public boolean escero(){
                return z==0;
            }
    public void escribeZp(){
        if(z==1)return;
        System.out.print(z);
    }
    public Zp inverso(){
        int aux = (p-z)%p;
        Zp ret=new Zp(aux,p);
        return ret;
    }
}
