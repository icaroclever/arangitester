package br.ufmg.lcc.arangitester.annotations;

public @interface Field {
    /**
     * Field Name.
     */
    String name();

    /**
     * Message to print.
     */
    String value();
}
