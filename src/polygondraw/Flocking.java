package polygondraw;

import processing.core.*;
import toxi.geom.*;


public class Flocking 
{
	Vec3D mPos;
	Vec3D mTailPos;
	Vec3D mVel;
	Vec3D mVelNormal;
	Vec3D mAcc;
	PApplet parent;
	float mMaxSpeed, mMaxSpeedSqrd;
	float mMinSpeed, mMinSpeedSqrd;
	float mDecay;
	float mRadius;
	float mLength;
	
	Flocking( PApplet p, Vec3D pos, Vec3D vel )
	{
		parent = p;
		mPos = pos;
		mTailPos = pos;
		mVel = vel;
		mVelNormal = (Vec3D) Vec3D.Y_AXIS;
		mAcc = (Vec3D) Vec3D.ZERO;
		
		mMaxSpeed = parent.random(2.0f, 3.0f);
		mMaxSpeedSqrd = mMaxSpeed * mMaxSpeed;
		mMinSpeed = parent.random(1.0f, 1.5f);
		mMinSpeedSqrd = mMinSpeed * mMinSpeed;
		
		mDecay = 0.99f;
		mRadius = 2.0f;
		mLength = 10.0f;
	}
	
	
	//------------------------------------------------------------
	void pullToCenter(Vec3D center)
	{
		Vec3D dirToCenter = mPos.sub(center);
		float distToCenter = dirToCenter.magnitude();
		float maxDistance = 300.0f;
		
		if( distToCenter > maxDistance ) {
			dirToCenter.normalize();
			float pullStrength = 0.0001f;
			mVel.subSelf( dirToCenter.scale( (distToCenter - maxDistance) * pullStrength ) );
		}
	}
	

	//------------------------------------------------------------
	void update( boolean flatten ) 
	{
		if ( flatten ) mAcc.z = 0.0f;
		mVel.addSelf(mAcc);
		mVelNormal = mVel.normalize();
		limitSpeed();
		
		mPos.addSelf(mVel);
		mTailPos = (mPos.sub(mVelNormal)).scale(mLength);
		
		if ( flatten ) mPos.z = 0.0f;
		
		mVel.scaleSelf(mDecay);
		mAcc = (Vec3D) Vec3D.ZERO;
	}
	
	
	//------------------------------------------------------------
	void limitSpeed() 
	{
		float vLengthSqrd = mVel.magSquared(); //this might be a problem? cinder has it as x*x + y*y + z*z
		if ( vLengthSqrd > mMaxSpeedSqrd ) {
			mVel = mVelNormal.scale(mMaxSpeed);
		} else if( vLengthSqrd < mMinSpeedSqrd ) {
			mVel = mVelNormal.scale(mMinSpeed);
		}
	}
	
	
	
	
	
	
	
}
