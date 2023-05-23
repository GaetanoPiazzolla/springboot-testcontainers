package gae.piaz.springtc.controller;

import java.io.Serializable;

public record CustomerData(String name, Integer id)
        implements Serializable {}
