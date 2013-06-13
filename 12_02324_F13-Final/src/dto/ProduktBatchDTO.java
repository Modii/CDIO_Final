package dto;

public class ProduktBatchDTO 
{
	int pbId;                     // i omraadet 1-99999999
	int receptId;
	String dato;
	int status;					// 0: ikke paabegyndt, 1: under produktion, 2: afsluttet
	
	public ProduktBatchDTO(int pbId, int receptId, String dato, int status)
	{
		this.pbId = pbId;
		this.receptId = receptId;
		this.dato = dato;
		this.status = status;
	}
	
	public int getPbId() { return pbId; }
	public void setPbId(int pbId) { this.pbId = pbId; }
	public int getReceptId() { return receptId; }
	public void setReceptId(int receptId) { this.receptId = receptId; }
	public String getDato() { return dato; }
	public void setDato(String dato) { this.dato = dato; }
	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }
	public String toString() { return pbId + "\t" + receptId + "\t" + status ; }
}

