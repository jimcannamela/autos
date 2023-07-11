package com.galvanize.autos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "automobiles")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Automobile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "model_year")
	private int year;
	private String make;
	private String model;
	private String color;
	@Column(name = "owner_name")
	private String owner;
	@JsonFormat(pattern = "MM/dd/yyyy")
	private Date purchaseDate;
	private String vin;

	public Automobile() {
	}
	public Automobile(int year, String make, String model, String vin) {
		this.year = year;
		this.make = make;
		this.model = model;
		this.vin = vin;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	@Override
	public String toString() {
		return "Automobile{" +
				"id=" + id +
				", year=" + year +
				", make='" + make + '\'' +
				", model='" + model + '\'' +
				", color='" + color + '\'' +
				", owner='" + owner + '\'' +
				", purchaseDate=" + purchaseDate +
				", vin='" + vin + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Automobile)) return false;
		Automobile that = (Automobile) o;
		return getYear() == that.getYear() && Objects.equals(id, that.id) && Objects.equals(getMake(), that.getMake()) && Objects.equals(getModel(), that.getModel()) && Objects.equals(getColor(), that.getColor()) && Objects.equals(getOwner(), that.getOwner()) && Objects.equals(getPurchaseDate(), that.getPurchaseDate()) && Objects.equals(getVin(), that.getVin());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, getYear(), getMake(), getModel(), getColor(), getOwner(), getPurchaseDate(), getVin());
	}
}
