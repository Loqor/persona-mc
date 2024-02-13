package mc.duzo.persona.common.skill;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class SkillSet {
    public static final int MAX_SKILLS = 8;

    private final List<Skill> skills = new ArrayList<>();
    private int selected = 0;

    public SkillSet(Skill... skills) {
        if (skills.length > MAX_SKILLS) {
            throw new IllegalArgumentException("Cannot have more than " + MAX_SKILLS + " skills");
        }

        this.skills.addAll(List.of(skills));
    }

    public void addSkill(Skill skill) {
        if (this.hasMaxSkills()) {
            throw new IllegalArgumentException("Cannot have more than " + MAX_SKILLS + " skills");
        }

        this.skills.add(skill);
    }
    public void removeSkill(Skill skill) {
        this.skills.remove(skill);
    }
    public void removeSkill(Identifier id) {
        skills.removeIf(skill -> skill.id().equals(id));
    }

    public void clear() {
        this.skills.clear();
    }

    public List<Skill> toList() {
        return skills;
    }

    public boolean hasMaxSkills() {
        return skills.size() >= MAX_SKILLS;
    }

    public Skill selected() {
        if (selected > skills.size() - 1) {
            selected = skills.size();
        }

        return skills.get(selected);
    }
    public void selectNext() {
        selected = (selected + 1 >= skills.size()) ? 0 : selected + 1;
    }
    public void selectPrevious() {
        selected = (selected - 1 < 0) ? skills.size() - 1 : selected - 1;
    }
    public void setSelected(int selected) {
        if (selected > skills.size() - 1) {
            selected = skills.size();
        }

        this.selected = selected;
    }

    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        NbtCompound skillData = new NbtCompound();

        for (Skill skill : this.skills) {
            skillData.put(skill.id().toString(), skill.toNbt());
        }

        nbt.put("skills", skillData);
        nbt.putInt("Selected", this.selected);

        return nbt;
    }

    public SkillSet loadNbt(NbtCompound nbt) {
        this.clear();

        NbtCompound skillData = nbt.getCompound("skills");

        skillData.getKeys().forEach(key -> {
            Skill skill = Skill.fromNbt(skillData.getCompound(key));

            if (skill == null) return;

            this.skills.add(skill);
        });

        this.setSelected(nbt.getInt("Selected"));

        return this;
    }
    public static SkillSet fromNbt(NbtCompound nbt) {
        SkillSet created = new SkillSet();

        created.loadNbt(nbt);

        return created;
    }
}
