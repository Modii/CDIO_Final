package dto;

/**
 * Operatoer Data Access Objekt
 * 
 * @author mn/tb
 * @version 1.2
 */

public class OperatoerDTO
{
	/** Operatoer-identifikationsnummer (opr_id) i omraadet 1-99999999. Vaelges af brugerne */
	int oprId;                     
	/** Operatoernavn (opr_navn) min. 2 max. 20 karakterer */
	String oprNavn;                
	/** Operatoer-initialer min. 2 max. 3 karakterer */
	String ini;                 
	/** Operatoer cpr-nr 10 karakterer */
	String cpr;                 
	/** Operatoer password min. 7 max. 8 karakterer */
	String password;    
	/** Operatoer aktoer-niveau (1 = admin / 2 = pharma / 3 = supervisor / 4 = operatoer */
	int aktoer;
	
	public OperatoerDTO(int oprId, String oprNavn, String ini, String cpr, String password, int aktoer)
	{
		this.oprId = oprId;
		this.oprNavn = oprNavn;
		this.ini = ini;
		this.cpr = cpr;
		this.password = password;
		this.aktoer = aktoer;
	}
	
    public OperatoerDTO(OperatoerDTO opr)
    {
    	this.oprId = opr.getOprId();
    	this.oprNavn = opr.getOprNavn();
    	this.ini = opr.getIni();
    	this.cpr = opr.getCpr();
    	this.password = opr.getPassword();
    	this.aktoer = opr.getAktoer();
    }
    
    public int getOprId() { return oprId; }
	public void setOprId(int oprId) { this.oprId = oprId; }
	public String getOprNavn() { return oprNavn; }
	public void setOprNavn(String oprNavn) { this.oprNavn = oprNavn; }
	public String getIni() { return ini; }
	public void setIni(String ini) { this.ini = ini; }
	public String getCpr() { return cpr; }
	public void setCpr(String cpr) { this.cpr = cpr; }
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
	public String toString() { return oprId + "\t" + oprNavn + "\t" + ini + "\t" + cpr + "\t" + password; }
	public int getAktoer() { return aktoer; }
	public void setAktoer(int aktoer) { this.aktoer = aktoer; }
}
