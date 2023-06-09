package br.com.anticheat.util;

import br.com.anticheat.util.pair.Pair;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.List;

@SuppressWarnings("Duplicates")
public class RayTrace implements CollisionBox {

    public double originX;
    public double originY;
    public double originZ;
    public double directionX;
    public double directionY;
    public double directionZ;

    public RayTrace(double originX, double originY, double originZ, double directionX, double directionY, double directionZ) {
        this.originX = originX;
        this.originY = originY;
        this.originZ = originZ;
        this.directionX = directionX;
        this.directionY = directionY;
        this.directionZ = directionZ;
    }

    public RayTrace(RayTrace ray) {
        this.originX = ray.originX;
        this.originY = ray.originY;
        this.originZ = ray.originZ;
        this.directionX = ray.directionX;
        this.directionY = ray.directionY;
        this.directionZ = ray.directionZ;
    }

    public RayTrace() {
        originX = 0;
        originY = 0;
        originZ = 0;
        directionX = 0;
        directionY = 0;
        directionZ = 0;
    }

    public RayTrace(LivingEntity e) {
        this(e.getEyeLocation());
    }

    public RayTrace(Location l) {
        this(l.toVector(),l.getDirection());
    }

    public RayTrace(Vector position, Vector direction) {
        this.originX = position.getX();
        this.originY = position.getY();
        this.originZ = position.getZ();
        this.directionX = direction.getX();
        this.directionY = direction.getY();
        this.directionZ = direction.getZ();
    }

    public CustomLocation getOrigin() {
        return new CustomLocation(originX, originY, originZ, 0, 0);
    }

    @Override
    public boolean isCollided(CollisionBox other) {
        if (other instanceof SimpleCollisionBox) {
            return intersect(this, (SimpleCollisionBox) other);
        } else if (other instanceof RayTrace) {
            return false; // lol no support
        }
        return false;
    }

    @Override
    public CollisionBox copy() {
        return new RayTrace(originX,originY,originZ,directionX,directionY,directionZ);
    }

    @Override
    public CollisionBox offset(double x, double y, double z) {
        originX+=x;
        originY+=y;
        originY+=z;
        return this;
    }

    @Override
    public void downCast(List<SimpleCollisionBox> list) {/*Do Nothing, Ray cannot be down-casted*/}

    @Override
    public boolean isNull() {
        return true;
    }

    public static double distance(RayTrace ray, SimpleCollisionBox box ) {
        Pair<Double,Double> pair = new Pair<>();
        if (intersect(ray,box,pair))
            return pair.getX();
        return -1;
    }

    public static boolean intersect(RayTrace ray, SimpleCollisionBox aab) {
        double invDirX = 1.0D / ray.directionX;
        double invDirY = 1.0D / ray.directionY;
        double invDirZ = 1.0D / ray.directionZ;
        double tFar;
        double tNear;
        if (invDirX >= 0.0D) {
            tNear = (aab.xMin - ray.originX) * invDirX;
            tFar = (aab.xMax - ray.originX) * invDirX;
        } else {
            tNear = (aab.xMax - ray.originX) * invDirX;
            tFar = (aab.xMin - ray.originX) * invDirX;
        }

        double tymin;
        double tymax;
        if (invDirY >= 0.0D) {
            tymin = (aab.yMin - ray.originY) * invDirY;
            tymax = (aab.yMax - ray.originY) * invDirY;
        } else {
            tymin = (aab.yMax - ray.originY) * invDirY;
            tymax = (aab.yMin - ray.originY) * invDirY;
        }

        if (tNear <= tymax && tymin <= tFar) {
            double tzmin;
            double tzmax;
            if (invDirZ >= 0.0D) {
                tzmin = (aab.zMin - ray.originZ) * invDirZ;
                tzmax = (aab.zMax - ray.originZ) * invDirZ;
            } else {
                tzmin = (aab.zMax - ray.originZ) * invDirZ;
                tzmax = (aab.zMin - ray.originZ) * invDirZ;
            }

            if (tNear <= tzmax && tzmin <= tFar) {
                tNear = tymin <= tNear && !Double.isNaN(tNear) ? tNear : tymin;
                tFar = tymax >= tFar && !Double.isNaN(tFar) ? tFar : tymax;
                tNear = tzmin > tNear ? tzmin : tNear;
                tFar = tzmax < tFar ? tzmax : tFar;
                if (tNear < tFar && tFar >= 0.0D) {
//                    result.x = tNear;
//                    result.y = tFar;
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    // Result X = near
    // Result Y = far
    public static boolean intersect(RayTrace ray, SimpleCollisionBox aab, Pair<Double,Double> result) {
        double invDirX = 1.0D / ray.directionX;
        double invDirY = 1.0D / ray.directionY;
        double invDirZ = 1.0D / ray.directionZ;
        double tFar;
        double tNear;
        if (invDirX >= 0.0D) {
            tNear = (aab.xMin - ray.originX) * invDirX;
            tFar = (aab.xMax - ray.originX) * invDirX;
        } else {
            tNear = (aab.xMax - ray.originX) * invDirX;
            tFar = (aab.xMin - ray.originX) * invDirX;
        }

        double tymin;
        double tymax;
        if (invDirY >= 0.0D) {
            tymin = (aab.yMin - ray.originY) * invDirY;
            tymax = (aab.yMax - ray.originY) * invDirY;
        } else {
            tymin = (aab.yMax - ray.originY) * invDirY;
            tymax = (aab.yMin - ray.originY) * invDirY;
        }

        if (tNear <= tymax && tymin <= tFar) {
            double tzmin;
            double tzmax;
            if (invDirZ >= 0.0D) {
                tzmin = (aab.zMin - ray.originZ) * invDirZ;
                tzmax = (aab.zMax - ray.originZ) * invDirZ;
            } else {
                tzmin = (aab.zMax - ray.originZ) * invDirZ;
                tzmax = (aab.zMin - ray.originZ) * invDirZ;
            }

            if (tNear <= tzmax && tzmin <= tFar) {
                tNear = tymin <= tNear && !Double.isNaN(tNear) ? tNear : tymin;
                tFar = tymax >= tFar && !Double.isNaN(tFar) ? tFar : tymax;
                tNear = tzmin > tNear ? tzmin : tNear;
                tFar = tzmax < tFar ? tzmax : tFar;
                if (tNear < tFar && tFar >= 0.0D) {
                    if (result != null) {
                        result.setX(tNear);
                        result.setY(tFar);
                    }
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public Vector collisionPoint(SimpleCollisionBox box) {
        Pair<Double, Double> p = new Pair<>();
        if (box==null||!intersect(this,box,p))
            return null;
        Vector vector = new Vector(directionX,directionY,directionZ);
        vector.normalize();
        vector.multiply(p.getX());
        vector.add(new Vector(originX,originY,originZ));
        return vector;
    }

    public Vector collisionPoint(double dist) {
        Vector vector = new Vector(directionX,directionY,directionZ);
        vector.normalize();
        vector.multiply(dist);
        vector.add(new Vector(originX,originY,originZ));
        return vector;
    }

}