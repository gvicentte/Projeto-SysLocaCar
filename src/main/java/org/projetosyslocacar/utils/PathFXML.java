package org.projetosyslocacar.utils;

import java.nio.file.Paths;

public class PathFXML {
    public static String pathFXML(){
        String path = "src/main/resources/view";
        return Paths.get(path).toAbsolutePath().toString();
    }
}