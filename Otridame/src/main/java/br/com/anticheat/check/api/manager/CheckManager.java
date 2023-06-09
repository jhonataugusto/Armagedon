package br.com.anticheat.check.api.manager;

import br.com.anticheat.check.impl.aimassist.*;
import br.com.anticheat.check.impl.aimassist.smooth.Mouse;
import br.com.anticheat.check.impl.aimassist.smooth.Optifine;
import br.com.anticheat.check.impl.autoclicker.*;
import br.com.anticheat.check.impl.badpackets.*;
import br.com.anticheat.check.impl.boatfly.BoatFlyA;
import br.com.anticheat.check.impl.fastladder.FastLadderA;
import br.com.anticheat.check.impl.fly.FlyA;
import br.com.anticheat.check.impl.fly.FlyB;
import br.com.anticheat.check.impl.hitbox.HitboxA;
import br.com.anticheat.check.impl.inventory.InventoryA;
import br.com.anticheat.check.impl.inventory.InventoryB;
import br.com.anticheat.check.impl.jesus.JesusA;
import br.com.anticheat.check.impl.killaura.*;
import br.com.anticheat.check.impl.motion.MotionA;
import br.com.anticheat.check.impl.motion.MotionB;
import br.com.anticheat.check.impl.motion.MotionC;
import br.com.anticheat.check.impl.nofall.NofallA;
import br.com.anticheat.check.impl.nofall.NofallB;
import br.com.anticheat.check.impl.noslow.NoSlowA;
import br.com.anticheat.check.impl.noslow.NoSlowB;
import br.com.anticheat.check.impl.pingspoof.PingSpoofA;
import br.com.anticheat.check.impl.pingspoof.PingSpoofB;
import br.com.anticheat.check.impl.reach.ReachA;
import br.com.anticheat.check.impl.reach.ReachB;
import br.com.anticheat.check.impl.scaffold.ScaffoldA;
import br.com.anticheat.check.impl.speed.SpeedA;
import br.com.anticheat.check.impl.speed.SpeedB;
import br.com.anticheat.check.impl.speed.SpeedC;
import br.com.anticheat.check.impl.sprint.OmniSprintA;
import br.com.anticheat.check.impl.step.StepA;
import br.com.anticheat.check.impl.step.StepB;
import br.com.anticheat.check.impl.timer.TimerA;
import br.com.anticheat.check.impl.timer.TimerB;
import br.com.anticheat.check.impl.velocity.VelocityA;
import br.com.anticheat.check.impl.velocity.VelocityB;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;
import lombok.Getter;
import br.com.anticheat.check.api.Check;
import br.com.anticheat.data.PlayerData;

import java.util.Collection;

//import me.liwk.karhu.check.impl.hitbox.HitboxA;

@Getter
public final class CheckManager {
    private final ClassToInstanceMap<Check> checks;

    public CheckManager(final PlayerData playerData) {
        checks = new ImmutableClassToInstanceMap.Builder<Check>()

                .put(KillauraA.class, new KillauraA(playerData))
                .put(KillauraB.class, new KillauraB(playerData))
                .put(KillauraC.class, new KillauraC(playerData))
                .put(KillauraD.class, new KillauraD(playerData))
                .put(KillauraE.class, new KillauraE(playerData))
                .put(KillauraF.class, new KillauraF(playerData))
                .put(KillauraG.class, new KillauraG(playerData))
                .put(KillauraH.class, new KillauraH(playerData))
                .put(KillauraI.class, new KillauraI(playerData))
                .put(KillauraJ.class, new KillauraJ(playerData))

                .put(HitboxA.class, new HitboxA(playerData))

                .put(ReachA.class, new ReachA(playerData))
                .put(ReachB.class, new ReachB(playerData))

                .put(VelocityA.class, new VelocityA(playerData))
                .put(VelocityB.class, new VelocityB(playerData))

                .put(Optifine.class, new Optifine(playerData))
                .put(Mouse.class, new Mouse(playerData))

                .put(AimA.class, new AimA(playerData))
                .put(AimB.class, new AimB(playerData))
                .put(AimC.class, new AimC(playerData))
                .put(AimD.class, new AimD(playerData))
                .put(AimE.class, new AimE(playerData))
                .put(AimF.class, new AimF(playerData))
                .put(AimG.class, new AimG(playerData))
                .put(AimH.class, new AimH(playerData))
                .put(AimI.class, new AimI(playerData))
                .put(AimJ.class, new AimJ(playerData))
                .put(AimK.class, new AimK(playerData))
                .put(AimL.class, new AimL(playerData))
                .put(AimM.class, new AimM(playerData))
                .put(AimO.class, new AimO(playerData))

                .put(BadA.class, new BadA(playerData))
                .put(BadB.class, new BadB(playerData))
                .put(BadC.class, new BadC(playerData))
                .put(BadD.class, new BadD(playerData))
                .put(BadE.class, new BadE(playerData))

                .put(FlyA.class, new FlyA(playerData))
                .put(FlyB.class, new FlyB(playerData))

                .put(BoatFlyA.class, new BoatFlyA(playerData))

                .put(StepA.class, new StepA(playerData))
                .put(StepB.class, new StepB(playerData))

                //.put(PhaseA.class, new PhaseA(playerData))

                .put(AutoclickerA.class, new AutoclickerA(playerData))
                .put(AutoclickerB.class, new AutoclickerB(playerData))
                .put(AutoclickerC.class, new AutoclickerC(playerData))
                .put(AutoclickerD.class, new AutoclickerD(playerData))
                .put(AutoclickerE.class, new AutoclickerE(playerData))
                .put(AutoclickerF.class, new AutoclickerF(playerData))

                .put(TimerA.class, new TimerA(playerData))
                .put(TimerB.class, new TimerB(playerData))

                .put(SpeedA.class, new SpeedA(playerData))
                .put(SpeedB.class, new SpeedB(playerData))
                .put(SpeedC.class, new SpeedC(playerData))

                .put(NoSlowA.class, new NoSlowA(playerData))
                .put(NoSlowB.class, new NoSlowB(playerData))

                .put(MotionA.class, new MotionA(playerData))
                .put(MotionB.class, new MotionB(playerData))
                .put(MotionC.class, new MotionC(playerData))

                .put(InventoryA.class, new InventoryA(playerData))
                .put(InventoryB.class, new InventoryB(playerData))

                .put(FastLadderA.class, new FastLadderA(playerData))

                .put(ScaffoldA.class, new ScaffoldA(playerData))

                .put(NofallA.class, new NofallA(playerData))
                .put(NofallB.class, new NofallB(playerData))

                .put(OmniSprintA.class, new OmniSprintA(playerData))

                .put(JesusA.class, new JesusA(playerData))

                .put(PingSpoofA.class, new PingSpoofA(playerData))
                .put(PingSpoofB.class, new PingSpoofB(playerData))
                .build();
    }

    public Collection<Check> getChecks() {
        if(checks != null)
            return checks.values();
        return null;
    }

    public int checkAmount() {
        if(this.getChecks() != null)
            return this.getChecks().size();
        return 0;
    }

    public Check getCheck(final Class<? extends Check> clazz) {
        return checks.getInstance(clazz);
    }


}
