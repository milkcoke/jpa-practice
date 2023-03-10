package com.example.jpapractice.domain.member;

import com.example.jpapractice.domain.BaseEntity;
import com.example.jpapractice.domain.team.Team;
import com.example.jpapractice.domain.locker.Locker;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import static jakarta.persistence.FetchType.LAZY;


@Entity
// uniqueConstraints = @UniqueConstraint(columnNames = {"member_name"})
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseEntity {
    // javax 또는 jakarta 가 JPA 표준 인터페이스
    // Hibernate 은 구현체.
    // JPA 에서는 데이터 변경시 항상 Transaction 안에서 작업해야한다.
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "member_id")
    private Long id;
    @Column(name = "member_name", updatable = true, nullable = false, length = 255)
    private String name;
    private Integer  age;

    @Embedded
    private Address address;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "team_id")
    private Team team;
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "locker_id", unique = true)
    private Locker locker;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    // ColumnDefinition 를 할 수도 있지만 권장하지 않음.
    // 특정 DB Dialect 와 호환되야하므로 쓸 때 유의해야함.
//    @Column(columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP", updatable = false)
//    @NotNull
//    private LocalDateTime createDate;

//    LocalDate 및 LodalDateTime 은 자동으로 java 8 부터 어노테이션 없어도 됨.
//    @Comment("최종 수정 시각")
//    private LocalDateTime lastModifiedDate;

    @Lob
    @Comment("비고 설명")
    private String description;

    public static Member registerMember(String name) {
        var member = new Member();
        member.name = name;
        member.roleType = RoleType.USER;
        return member;
    }

//    Q. 생성자나 커스텀 메소드 말고 다음과 같이 쓰는게 더 낫겠지?
    @PrePersist()
    public void prePersist() {
        this.updateCreateTimestamp(this.getName());
    }

    @PreUpdate()
    public void preUpdate() {
        this.updateModifiedTimestamp(this.getName());
    }

    @Transactional
    public void changeMemberInfo(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public void changeHomeAddress(Address address) {
        this.address = address;
    }
    /**
     * 연관관계 편의 메소드
     * @param team
     */
    public void registerTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

    public void changeTeam(Team team) {
        this.team =team;
    }

    public void changeMemberLocker(Locker locker) {
        this.locker = locker;
    }
}
