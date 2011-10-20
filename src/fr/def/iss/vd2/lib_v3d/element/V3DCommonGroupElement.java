// Copyright 2010 DEF
//
// This file is part of V3dScene.
//
// V3dScene is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// V3dScene is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with V3dScene.  If not, see <http://www.gnu.org/licenses/>.

package fr.def.iss.vd2.lib_v3d.element;

import java.util.List;

import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.V3DVect3;
import fr.def.iss.vd2.lib_v3d.camera.V3DCamera;

/**
 *
 * @author fberto
 */
abstract class V3DCommonGroupElement extends V3DElement {

    V3DBoundingBox boundingBox = new V3DBoundingBox();

    public V3DCommonGroupElement(V3DContext context) {
        super(context);
    }

    @Override
    protected void doDisplay( V3DCamera camera) {
        for(V3DElement child: getChildren()) {
            child.display( camera);
        }
    }

    @Override
    protected void doInit() {
        for(V3DElement child: getChildren()) {
            child.init();
        }
    }

    @Override
    protected void doSelect( V3DCamera camera, long parentId) {
        for(V3DElement child: getChildren()) {
            child.select( camera, parentId);
        }
    }

    public final List<V3DElement> getChildren() {
        return doGetChildren();
    }

    protected abstract List<V3DElement> doGetChildren();

    @Override
    public V3DBoundingBox getBoundingBox() {
        float minX = Float.POSITIVE_INFINITY;
        float maxX = Float.NEGATIVE_INFINITY;
        float minY = Float.POSITIVE_INFINITY;
        float maxY = Float.NEGATIVE_INFINITY;
        float minZ = Float.POSITIVE_INFINITY;
        float maxZ = Float.NEGATIVE_INFINITY;
        boolean exists = false;

        for(V3DElement child: getChildren()) {
            V3DBoundingBox childBox = child.getBoundingBox();

            if(child.isTangible() && childBox.isExist()) {
                exists = true;
                V3DVect3 childSize = childBox.getSize().multiply(child.getScale());
                V3DVect3 childPos = child.getPosition();
                V3DVect3 childBoxPos = childBox.getPosition();
                float centerX = childPos.x + childBoxPos.x;
                float centerY = childPos.y + childBoxPos.y;
                float centerZ = childPos.z + childBoxPos.z;
                float halfX = childSize.x/2;
                float halfY = childSize.y/2;
                float halfZ = childSize.z/2;

                minX = Math.min(minX, centerX - halfX);
                maxX = Math.max(maxX, centerX + halfX);

                minY = Math.min(minY, centerY - halfY);
                maxY = Math.max(maxY, centerY + halfY);

                minZ = Math.min(minZ, centerZ - halfZ);
                maxZ = Math.max(maxZ, centerZ + halfZ);
            }
        }

        boundingBox.setExist(exists);

        if(exists) {
            boundingBox.setSize(maxX-minX, maxY-minY, maxZ-minZ);
            boundingBox.setPosition(minX+(maxX-minX)/2f, minY+(maxY-minY)/2f, minZ+(maxZ-minZ)/2f);
            if(boundingBox.getSize().z > 0) {
                boundingBox.setFlat(false);
            }
        } else {
            boundingBox.setSize(0,0,0);
            boundingBox.setPosition(0,0,0);
        }
        return boundingBox;
    }

    public void clear() {
        getChildren().clear();
    }

    public void remove(V3DElement element) {
        getChildren().remove(element);
    }
    

}
