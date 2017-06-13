package pl.edu.agh.tkk17.sample;

import java.util.Arrays;
import java.util.Iterator;

public class Parser
{
    private Iterator<Token> tokens;
    private Token ctoken;

    public Parser(Iterable<Token> tokens)
    {
        this.tokens = tokens.iterator();
        this.forward();
    }

    private void forward()
    {
        this.ctoken = this.tokens.next();
    }

    private String value()
    {
        return this.ctoken.getValue();
    }

    private boolean check(TokenType type)
    {
        return this.ctoken.getType() == type;
    }

    private void expect(TokenType type)
    {
        if (!this.check(type)) {
            throw new UnexpectedTokenException(this.ctoken, type);
        }
    }

    private void checkIfSpace() {
        /*if (Character.isWhitespace(this.value().charAt(0))) {
            this.forward();
            checkIfSpace();
        }*/
    }

    private Node tempNode;

    private void setTempNode(Node temp) {
        this.tempNode = temp;
    }

    private Node getTempNode() {
        return this.tempNode;
    }

    private Node parseNumber()
    {
        checkIfSpace();
        this.expect(TokenType.NUM);
        String value = this.value();
        this.forward();
        if (check(TokenType.NUM)) {
            value = value + this.value();
            this.forward();
        }
        return new NodeNumber(value);
    }

    private Node parseTerm(Node rightLeaf)
    {
        if (rightLeaf == null) {
            Node left = this.parseNumber();
            if (this.check(TokenType.MUL)) {
                this.forward();
                Node right = new NodeNumber(this.value());
                this.forward();
                Node tempNode = new NodeDiv(left, right);
                setTempNode(tempNode);
                this.parseTerm(tempNode);
                return getTempNode();
            } else if (this.check(TokenType.DIV)) {
                this.forward();
                Node right = new NodeNumber(this.value());
                this.forward();
                Node tempNode = new NodeDiv(left, right);
                setTempNode(tempNode);
                this.parseTerm(tempNode);
                return getTempNode();
            }
            return left;
        } else {
            if (this.check(TokenType.MUL)) {
                this.forward();
                String t = this.value();
                this.forward();
                Node tempNode = new NodeMul(new NodeNumber(t), rightLeaf);
                setTempNode(tempNode);
                this.parseTerm(tempNode);
                return getTempNode();
            } else if (this.check(TokenType.DIV)) {
                this.forward();
                String t = this.value();
                this.forward();
                Node tempNode = new NodeDiv(new NodeNumber(t), rightLeaf);
                setTempNode(tempNode);
                this.parseTerm(tempNode);
                return getTempNode();
            }
        }
        return rightLeaf;
    }

    private Node parseExpression()
    {
        Node left = this.parseTerm(null);
        if (this.check(TokenType.ADD)) {
            this.forward();
            Node right = this.parseExpression();
            return new NodeAdd(left, right);
        } else {
            return left;
        }
    }

    private Node parseProgram()
    {
        Node root = this.parseExpression();
        this.expect(TokenType.END);
        return root;
    }

    public static Node parse(Iterable<Token> tokens)
    {
        Parser parser = new Parser(tokens);
        Node root = parser.parseProgram();
        return root;
    }
}
