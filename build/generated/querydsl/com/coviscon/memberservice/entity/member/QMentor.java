package com.coviscon.memberservice.entity.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMentor is a Querydsl query type for Mentor
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QMentor extends BeanPath<Mentor> {

    private static final long serialVersionUID = -1053582256L;

    public static final QMentor mentor = new QMentor("mentor");

    public final StringPath career = createString("career");

    public final StringPath company = createString("company");

    public final StringPath introduce = createString("introduce");

    public final StringPath job = createString("job");

    public final StringPath mPrice = createString("mPrice");

    public QMentor(String variable) {
        super(Mentor.class, forVariable(variable));
    }

    public QMentor(Path<? extends Mentor> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMentor(PathMetadata metadata) {
        super(Mentor.class, metadata);
    }

}

