package com.irr310.common.world;

import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.Vect3;
import com.irr310.common.world.view.PartView;

public class Part extends GameEntity {

	// private final Vect3 position;
	private Double mass;
	private final Vect3 rotationSpeed;
	private final Vect3 linearSpeed;
	private final TransformMatrix transform;
	private Vect3 shape;

	public Part(long id) {
	    super(id);
		// position = Vect3.origin();
		rotationSpeed = Vect3.origin();
		linearSpeed = Vect3.origin();
		transform = TransformMatrix.identity();
		mass = 0.;

		shape = Vect3.one();
	}

	public void setMass(Double mass) {
		this.mass = mass;
	}

	public Double getMass() {
		return mass;
	}

	public Vect3 getRotationSpeed() {
		return rotationSpeed;
	}

	public Vect3 getLinearSpeed() {
		return linearSpeed;
	}

	public TransformMatrix getTransform() {
		return transform;
	}

	public Vect3 getShape() {
		return shape;
	}

	public void setShape(Vect3 shape) {
		this.shape = shape;
	}

    public PartView toView() {
        PartView partView = new PartView();
        partView.id = getId();
        partView.linearSpeed = linearSpeed;
        partView.mass = mass;
        partView.rotationSpeed = rotationSpeed;
        partView.shape = shape;
        partView.transform = transform;
        return partView;
    }

}
