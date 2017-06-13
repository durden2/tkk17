package pl.edu.agh.tkk17.sample;

public enum TokenType
{
    END("END"),
    NUM("NUM"),
    ADD("ADD"),
    DIV("DIV"),
    SUB("SUB"),
    MUL("MUL");

    private final String name;

    TokenType(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }
}
