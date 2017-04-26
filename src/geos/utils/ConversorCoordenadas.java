package geos.utils;
public class ConversorCoordenadas {
	
	public static final double pi = 3.14159265358979;	
	/* Ellipsoid model constants (actual values here are for WGS84) */
	public static final double sm_a = 6378137.0;
	public static final double sm_b = 6356752.314;
	public static final double sm_EccSquared = 6.69437999013e-03;
	private final double UTMScaleFactor = 0.9996;
	private double excentr, daexcentr, radiopolar;
	int huso = 19;
	boolean hemisferioSur = true;
	public double[] ConversorCoordenadas(double lat, double lng, int huso,boolean hemisferioSur) 
	{
		this.init_geovars();
		//TRANSFORMAMOS
		return UTMXYToLatLon(lng,lat,huso,hemisferioSur);
	}
	public double[] ConversorCoordenadas(double lat, double lng){
		this.init_geovars();
		//ASUMIMOS QUE ES CHILE CONTINENTAL
		this.huso = 19;
		this.hemisferioSur = true;
		//TRANSFORMAMOS
		return UTMXYToLatLon(lng,lat,this.huso,this.hemisferioSur);
	}
	public ConversorCoordenadas(){
		this.init_geovars();
	}
	private void init_geovars(){
		double semiejemayor = 6378388.0;
		double semiejemenor = 6356911.946130;
		this.excentr = Math.sqrt(Math.pow(semiejemayor,2)-Math.pow(semiejemenor,2))/semiejemayor;
		this.daexcentr = Math.sqrt(Math.pow(semiejemayor,2)-Math.pow(semiejemenor,2))/semiejemenor;
		this.radiopolar = Math.pow(semiejemayor, 2)/semiejemenor;		
	}
	public double[][] UTMXYToLatLon(double[][] coords, int zone, boolean hemiferioSur){
		double finalCoords[][] = new double[coords.length][2];
		for(int i=0; i< coords.length; i++){
			//TODO Auto-generated method stub
			double cmeridian;
			finalCoords[i][0] -= 500000;
			/* If in southern hemisphere, adjust y accordingly. */
			if (hemiferioSur) finalCoords[i][1] -= 10000000;
			cmeridian = UTMCentralMeridian(zone);
			finalCoords[i] = convertToDec(finalCoords[i][0], finalCoords[i][1], cmeridian);
		}
		if(finalCoords.length == 0){
			return null;
		}
		else
		{
			return finalCoords;
		}
		
			
	}
	public double[] UTMXYToLatLon(double lat, double lng, int zone, boolean hemiferioSur){
		double finalCoords[] = new double[2];
		//TODO Auto-generated method stub
		double cmeridian;
		lng -= 500000;
		/* If in southern hemisphere, adjust y accordingly. */
		if (hemiferioSur) lat -= 10000000;
		
		cmeridian = UTMCentralMeridian(zone);
		finalCoords = convertToDec(lng, lat, cmeridian);
		if(finalCoords.length == 0){
			return null;
		}
		else
		{
			return finalCoords;
		}
	}
	private double UTMCentralMeridian(int zone) {
		// TODO Auto-generated method stub
		double cmeridian;
		cmeridian = (zone * 6.0)-183;
		return cmeridian;
	}
	private double[] convertToDec(double x,double y, double med){
		double phi, cos2phi, daexc2 ,ni,a,Aone,Atwo,Jone,Jtwo,Jsix,alpha,beta,gamma,Bphi,b,Dseta,xi,eta,senxieta,Ddelta,tau;
		phi = y/(6366197.724*this.UTMScaleFactor);
		cos2phi = Math.pow(Math.cos(phi), 2.0);
		daexc2 = Math.pow(this.daexcentr,2.0);
		ni = (this.radiopolar*this.UTMScaleFactor)/ Math.sqrt((1.0+0.00676817*cos2phi));
		a = x/ni;
		Aone = Math.sin(2.0*phi);
		Atwo = Aone*cos2phi;
		Jone = phi + (Aone/2.0);
		Jtwo = (3.0*Jone + Atwo)/4.0;
		Jsix = (5.0*Jtwo+Atwo*cos2phi)/3.0;
		alpha = (3.0/4.0)*daexc2;
		beta = (5.0/3.0)*Math.pow(alpha, 2.0);
		gamma = (35.0/27.0)*Math.pow(alpha, 3.0);
		Bphi = this.UTMScaleFactor*this.radiopolar*(phi-(alpha*Jone)+(beta*Jtwo)-(gamma*Jsix));
		b = (y-Bphi)/ni;
		Dseta = ((Math.pow(this.daexcentr,2.0)*Math.pow(a,2.0))/2.0)*cos2phi;
		xi = a*(1.0-(Dseta/3.0));
		eta = (b*(1.0-Dseta))+phi;
		senxieta = (Math.exp(xi)-Math.exp(xi*-1.0))/2.0;
		Ddelta = Math.atan((senxieta/Math.cos(eta)));
		tau = Math.atan(Math.cos(Ddelta)*Math.tan(eta));
		//HACEMOS EL CALCULO FINAL DE LONGITUD
		double lng = ((Ddelta/Math.PI)*180.0)+med;
		//HACEMOS EL CALCULO FINAL DE LA LATITUD
		double lat = phi + (1.0+(daexc2*cos2phi)-((3.0/2.0)*daexc2*Math.sin(phi)*Math.cos(phi)*(tau-phi)))*(tau-phi);
		lat = (lat/Math.PI)*180.0;
		double coord[] = new double[2];
		coord[0] = lat;
		coord[1] = lng;
		return coord;
	}
}
