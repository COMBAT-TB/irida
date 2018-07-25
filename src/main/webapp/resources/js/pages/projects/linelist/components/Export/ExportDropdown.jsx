import React from "react";
import PropTypes from "prop-types";
import { Button, Dropdown, Icon, Menu } from "antd";

const { i18n } = window.PAGE;

export function ExportDropDown(props) {
  const onClick = ({ key }) => {
    if (typeof props[key] !== "undefined") {
      props[key]();
    } else {
      throw new Error(`No export function for key: ${key}`);
    }
  };

  const menu = (
    <Menu onClick={onClick}>
      <Menu.Item key="excel">{i18n.linelist.toolbar.exportExcel}</Menu.Item>
      <Menu.Item key="csv">{i18n.linelist.toolbar.exportCsv}</Menu.Item>
    </Menu>
  );
  return (
    <Dropdown overlay={menu}>
      <Button>
        {i18n.linelist.toolbar.export} <Icon type="down" />
      </Button>
    </Dropdown>
  );
}

ExportDropDown.propTypes = {
  csv: PropTypes.func.isRequired
};