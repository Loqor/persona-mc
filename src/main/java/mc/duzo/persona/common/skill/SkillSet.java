package mc.duzo.persona.common.skill;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class SkillSet {
    public static final int MAX_SKILLS = 8;

    private final List<Skill> skills = new ArrayList<>();
    private Identifier selected;
    public SkillSet(Skill... skills) {
        if (skills.length > MAX_SKILLS) {
            throw new IllegalArgumentException("Cannot have more than " + MAX_SKILLS + " skills");
        }

        this.skills.addAll(List.of(skills));
    }

    @Override
    public String toString() {
        return "SkillSet{" +
                "skills=" + skills +
                ", selected=" + selected +
                '}';
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

    private int getSelectedPosition() {
        return this.skills.indexOf(this.getSelected());
    }

    public Skill getSelected() {
        return this.skills.stream().filter(skill -> skill.id().equals(selected)).findFirst().orElse(this.skills.get(0));
    }
    public void selectNext() {
        selected = skills.get((getSelectedPosition() + 1 >= skills.size()) ? 0 : getSelectedPosition() + 1).id();
    }
    public void selectPrevious() {
        selected = skills.get((getSelectedPosition() - 1 < 0) ? skills.size() - 1 : getSelectedPosition() - 1).id();
    }
    public void setSelected(int selected) {
        if (selected > skills.size() - 1) {
            selected = skills.size();
        }

        this.selected = skills.get(selected).id();
    }
    public void setSelected(Identifier selected) {
        this.selected = selected;
    }

    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        NbtCompound skillData = new NbtCompound();

        for (Skill skill : this.skills) {
            skillData.put(skill.id().toString(), skill.toNbt());
        }

        nbt.put("skills", skillData);
        nbt.putString("Selected", this.getSelected().id().toString());

        return nbt;
    }

    public SkillSet loadNbt(NbtCompound nbt) {
        this.clear();

        NbtCompound skillData = nbt.getCompound("skills");

        for (String key : skillData.getKeys()) {
            Skill skill = Skill.fromNbt(skillData.getCompound(key));

            if (skill == null) continue;

            this.skills.add(skill);
        }

        this.setSelected(new Identifier(nbt.getString("Selected")));

        return this;
    }
    public static SkillSet fromNbt(NbtCompound nbt) {
        SkillSet created = new SkillSet();

        created.loadNbt(nbt);

        return created;
    }
}
