package org.api.script.impl.mission.blast_furnace_mission.worker;

import org.api.script.framework.worker.Worker;
import org.api.script.framework.worker.WorkerHandler;
import org.api.script.impl.mission.blast_furnace_mission.BlastFurnaceMission;
import org.api.script.impl.mission.blast_furnace_mission.worker.impl.*;
import org.api.script.impl.mission.blast_furnace_mission.worker.impl.smelt.SmeltBars;
import org.api.script.impl.mission.blast_furnace_mission.worker.impl.smelt.WithdrawOre;
import org.api.script.impl.worker.banking.DepositWorker;
import org.rspeer.runetek.api.Varps;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Equipment;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;
import org.rspeer.runetek.api.movement.Movement;

public class BlastFurnaceWorkerHandler extends WorkerHandler {

    private final PayCoffer payCoffer;
    private final PayForeman payForeman;
    private final WithdrawCoalBag withdrawCoalBag;
    private final EquipIceGloves equipIceGloves;
    //private final UseWaterBucket useWaterBucket;
    private final CollectBars collectBars;
    private final WithdrawOre withdrawOre;
    private final SmeltBars smeltBars;
    private final DrinkStamina drinkStamina;
    private final BlastFurnaceMission mission;

    public BlastFurnaceWorkerHandler(BlastFurnaceMission mission) {
        this.mission = mission;
        payCoffer = new PayCoffer(mission);
        payForeman = new PayForeman(mission);
        withdrawCoalBag = new WithdrawCoalBag(mission);
        equipIceGloves = new EquipIceGloves(mission);
        //useWaterBucket = new UseWaterBucket();
        collectBars = new CollectBars(mission);
        withdrawOre = new WithdrawOre(mission);
        smeltBars = new SmeltBars(mission);
        drinkStamina = new DrinkStamina();
    }

    @Override
    public Worker decide() {
        if (Skills.getLevel(Skill.SMITHING) < 60 && !payForeman.paidForeman || PayForeman.shouldPayForeman())
            return payForeman;

        if (Varps.get(PayCoffer.COFFER_VARP) <= PayCoffer.COFFER_MIN)
            return payCoffer;

        if (!Equipment.contains("Ice gloves"))
            return equipIceGloves;

        if (((!Movement.isStaminaEnhancementActive() && Bank.isOpen()) || Inventory.contains(DrinkStamina.stamina) || Inventory.contains(DrinkStamina.vial)) && !drinkStamina.outOfStamina)
            return drinkStamina;

        if (Inventory.getFirst(WithdrawCoalBag.COAL_BAG) == null)
            return withdrawCoalBag;

        /*if (Varps.getBankCache(CollectBars.COLLECT_BARS_VARP) > 0 && Varps.getBankCache(CollectBars.COLLECT_BARS_VARP) != CollectBars.COLLECT_BARS_COOLED_SETTING)
            return useWaterBucket;*/

        if (collectBars.isDoneSmelting()) {
            if (Inventory.isFull())
                return new DepositWorker();

            return collectBars;
        }

        if (!mission.isSmelting)
            return withdrawOre;

        return smeltBars;
    }
}

