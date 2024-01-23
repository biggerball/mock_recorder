package com.github.biggerball.storage;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;


@SpringBootApplication
public class SpringbootApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {//--spring.datasource.url=jdbc:sqlite:/data/db1/test.db
        if (args != null) {
            List<String> appArgs = new ArrayList<>();
            for (String arg : args) {
                appArgs.addAll(convertArg(arg));
            }
            args = appArgs.toArray(new String[]{});
        }
        SpringApplication.run(SpringbootApplication.class, args);
    }


    private static List<String> convertArg(String arg) {
        int index = arg.indexOf('=');
        Optional<Arg> optional = Arg.containKey(arg.substring(0, index));
        if (optional.isPresent()) {
            return optional.get().func.apply(arg.substring(index + 1));
        } else {
            return Collections.singletonList(arg);
        }
    }

    private enum Arg {
        DIR("--dir", path -> Arrays.asList(
                "--spring.datasource.url=jdbc:sqlite:" + path + File.separator + "database.db",
                "--logging.file.name=" + path + File.separator + "log.log"));
        final String name;
        final Function<String, List<String>> func;

        Arg(String name, Function<String, List<String>> func) {
            this.name = name;
            this.func = func;
        }

        public static Optional<Arg> containKey(String key) {
            for (Arg arg : Arg.values()) {
                if (arg.name.equals(key)) {
                    return Optional.of(arg);
                }
            }
            return Optional.empty();
        }
    }
}
