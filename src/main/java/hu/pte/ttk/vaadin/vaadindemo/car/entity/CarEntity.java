package hu.pte.ttk.vaadin.vaadindemo.car.entity;

import hu.pte.ttk.vaadin.vaadindemo.manufacturer.entity.ManufacturerEntity;
import hu.pte.ttk.vaadin.vaadindemo.core.entity.CoreEntity;

import javax.persistence.*;

@Table(name = "car")
@Entity
public class CarEntity extends CoreEntity {

    @Column(name = "type")
    private String type;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    private ManufacturerEntity manufacturer;

    @Column(name = "doors")
    private String doors;

    @Column(name = "year")
    private String year;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ManufacturerEntity getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(ManufacturerEntity author) {
        this.manufacturer = author;
    }

    public String getDoors() {
        return doors;
    }

    public void setDoors(String doors) {
        this.doors = doors;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
