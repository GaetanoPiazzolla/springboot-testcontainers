package gae.piaz.springtc.controller;

import java.io.Serializable;

public record CustomerDTO(String name, Integer id)
        implements Serializable {
}
