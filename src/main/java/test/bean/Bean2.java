package test.bean;

import framework.annotations.Bean;
import framework.annotations.ScopeType;

@Bean(scope = "prototype")
public class Bean2 {

    public Bean2() {

    }
}
