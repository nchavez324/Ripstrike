package project.model.util;

/*
 * Unpublished Copyright 2012 RipStrike
 * Nicolas Chavez, USA
 */

public class WeaponData {
    // accuracy, damage, dispersion, scope, oneHanded, explosive, melee
    public Vector2 weaponPosition;

    public float accuracy, damage;

    public int dispersion, rateOfFire, bulletsPerClip, numClips, reloadTime, colLen;

    public boolean oneHanded, explosive, melee, primary;

    public WeaponData(Vector2 weaponPosition, float accuracy, float damage, int dispersion, int rateOfFire,
            int reloadTime, int bulletsPerClip, int numClips, int colLen, boolean oneHanded, boolean explosive,
            boolean melee) {
        this.weaponPosition = weaponPosition;
        this.oneHanded = oneHanded;
        this.explosive = explosive;
        this.melee = melee;
        this.accuracy = accuracy;
        this.damage = damage;
        this.dispersion = dispersion;
        this.rateOfFire = rateOfFire;
        this.reloadTime = reloadTime;
        this.bulletsPerClip = bulletsPerClip;
        this.numClips = numClips;
        this.colLen = colLen;
    }
}
