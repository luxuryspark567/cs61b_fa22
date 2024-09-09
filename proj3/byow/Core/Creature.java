package byow.Core;

import java.util.LinkedList;

public class Creature {
    private int damage;
    private int health;
    private int age;
    private int weight;
    private Size size;
    private int affection;
    private Position position;
    LinkedList<Item> inventory;

    public Creature() {
        this.damage = 0;
        this.health = 0;
        this.age = 0;
        this.weight = 0;
        this.size = new Size(0, 0);
        this.affection = 0;
        this.position = new Position(0, 0);
        this.inventory = new LinkedList<>();
    }

    public Creature(int damage, int health, int age, int weight, Size size, int affection, Position pos) {
        this.damage = damage;
        this.health = health;
        this.age = age;
        this.weight = weight;
        this.size = Size.copyOf(size);
        this.affection = affection;
        this.position = Position.copyOf(pos);
        this.inventory = new LinkedList<>();
    }

    public int getDamage() {
        return this.damage;
    }
    public int getHealth() {
        return this.health;
    }
    public int getAge() {
        return this.age;
    }
    public int getWeight() {
        return this.weight;
    }

    public Size getSize() {
        return Size.copyOf(this.size);
    }
    public int getAffection() {
        return this.affection;
    }

    public Position getPosition() {
        return Position.copyOf(this.position);
    }

    public LinkedList<Item> getInventory() {
        return this.inventory;
    }



    public void setDamage(int damage) {
        this.damage = damage;
    }
    public void setHealth(int health) {
        this.health = health;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setSize(Size size) {
        this.size.w = size.w;
        this.size.h = size.h;
    }
    public void setAffection(int affection) {
        this.affection = affection;
    }

    public void setPosition(Position pos) {
        this.position.x = pos.x;
        this.position.y = pos.y;
    }

    public void addInventory(Item item) {
        this.inventory.add(item);
    }

    public void removeInventory(Item item) {
        this.inventory.remove(item);
    }
}
