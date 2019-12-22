package beans;

public class AgeGroup {

	private int ageId;
	private float price;
	private int upperAge;
	private int lowerAge;
	private String name;
	
	public AgeGroup(int ageId, float price, int upperAge, int lowerAge, String name) {
		this.ageId = ageId;
		this.price = price;
		this.upperAge = upperAge;
		this.lowerAge = lowerAge;
		this.name = name;
	}
	public AgeGroup() {
		this.ageId = 0;
		this.price = (float) 0.0;
		this.upperAge = 150;
		this.lowerAge = 0;
		this.name = "default";
	}
	
	public int getAgeId() {
		return ageId;
	}
	public void setAgeId(int ageId) {
		this.ageId = ageId;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public int getUpperAge() {
		return upperAge;
	}
	public void setUpperAge(int upperAge) {
		this.upperAge = upperAge;
	}
	public int getLowerAge() {
		return lowerAge;
	}
	public void setLowerAge(int lowerAge) {
		this.lowerAge = lowerAge;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
