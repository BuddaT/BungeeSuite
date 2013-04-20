package com.mcdimensions.BungeeSuite.warps;

public class WarpLocation {
	private String server;
	private String world;
	private double X;
	private double Y;
	private double Z;
	private float yaw;
	private float pitch;

	public WarpLocation(String server, String world, double x, double y,
			double z, float yaw, float pitch) {
		this.server = server;
		this.world = world;
		this.X = x;
		this.Y = y;
		this.Z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public String getWorld() {
		return world;
	}

	public String getServer() {
		return server;
	}

	public double getX() {
		return X;
	}

	public double getY() {
		return Y;
	}

	public double getZ() {
		return Z;
	}

	public float getYaw() {
		return yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public String serialize() {
		return server + "~" + world + "~" + X + "~" + Y + "~" + Z + "~" + yaw
				+ "~" + pitch + "";
	}
}
