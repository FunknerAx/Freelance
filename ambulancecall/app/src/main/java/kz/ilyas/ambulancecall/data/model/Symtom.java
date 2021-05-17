package kz.ilyas.ambulancecall.data.model;

import java.io.Serializable;
import java.util.HashMap;

public class Symtom implements Serializable {
    private String name;
    private int status;

    public Symtom() {
    }

    public Symtom(String name, int status) {
        this.name = name;
        this.status = status;
    }

    public Symtom(HashMap<String, Object> in) {
        this.name = (String) in.get("name");
        this.status = ((Long) in.get("status")).intValue();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    ;

    public void setStatus(int status) {
        this.status = status;
    }
}
