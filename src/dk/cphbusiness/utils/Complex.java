package dk.cphbusiness.utils;

import java.io.Serializable;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

@SuppressWarnings("LocalVariableHidesMemberVariable")
public final class Complex implements Serializable {
  private double re;
  private double im;
  
  public Complex(double re, double im) {
    set(re, im);
    }
  
  private Complex(Complex template) {
    set(template.re, template.im);
    }
  
  public void set(double re, double im) {
    this.re = re;
    this.im = im;
    }
  
  @Override
  public String toString() {
    return ""+re+(im < 0.0 ? " - " : " + ")+abs(im);
    }
  
  public double getRe() { return re; }
  
  public double getIm() { return im; }
  
  public Complex conjugated() {
    im = -im;
    return this;
    }
  
  public Complex conjugate() {
    return new Complex(this).conjugated();
    }
  
  public Complex added(Complex other) {
    re += other.re;
    im += other.im;
    return this;
    }
  
  public Complex add(Complex other) {
    return new Complex(this).added(other);
    }
  
  public Complex subtracted(Complex other) {
    re -= other.re;
    im -= other.im;
    return this;
    }
  
  public Complex subtract(Complex other) {
    return new Complex(this).subtracted(other);
    }
  
  public Complex multipied(Complex other) {
    double re = this.re*other.re - this.im*other.im;
    double im = this.re*other.im + other.re*this.im;
    this.re = re;
    this.im = im;
    return this;
    }
  
  public Complex multiply(Complex other) {
    return new Complex(this).multipied(other);
    }
  
  public Complex powered(int power) {
    double re = this.re;
    double im = this.im;
    while (power > 1) {
      double tre = re*this.re - im*this.im;
      double tim = re*this.im + this.re*im;
      re = tre;
      im = tim;
      power--;
      }
    this.re = re;
    this.im = im;
    return this;
    }
  
  public Complex power(int power) {
    return new Complex(this).powered(power);
    }
  
  public double squareModulus() {
    return re*re + im*im;
    }
  
  public double modulus() {
    return sqrt(squareModulus());
    }
  
  public Complex divided(Complex other) {
    double l = squareModulus();
    double re = (this.re*other.re + this.im*other.im)/l;
    double im = (other.re*this.im - this.re*other.im)/l;
    this.re = re;
    this.im = im;
    return this;
    }
  
  public Complex divide(Complex other) {
    return new Complex(this).divided(other);
    }
  
  }
