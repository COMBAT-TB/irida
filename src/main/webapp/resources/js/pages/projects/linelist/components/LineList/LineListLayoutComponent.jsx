import React from "react";
import { Layout } from "antd";
import { Table } from "../Table";
import { ToolPanel } from "../ToolPanel";
import { Toolbar } from "../Toolbar";
import { InfoBar } from "../InfoBar";

const { Sider, Content } = Layout;

export class LineListLayoutComponent extends React.Component {
  tableRef = React.createRef();
  state = {
    collapsed: false
  };

  constructor(props) {
    super(props);
  }

  /**
   * Toggle the open state of the tool panel.
   */
  toggleToolPanel = () => {
    this.setState({
      collapsed: !this.state.collapsed
    });
  };

  addSamplesToCart = () => {
    this.tableRef.current.addSamplesToCart();
  };

  exportCSV = () => {
    this.tableRef.current.exportCSV();
  };

  exportXLSX = () => {
    this.tableRef.current.exportXLSX();
  };

  render() {
    return (
      <React.Fragment>
        <Toolbar
          exportCSV={this.exportCSV}
          exportXLSX={this.exportXLSX}
          addSamplesToCart={this.addSamplesToCart}
          selectedCount={this.props.selectedCount}
        />
        <Layout className="ag-theme-balham fucked-up">
          <Content>
            <Table {...this.props} ref={this.tableRef} />
          </Content>
          <Sider
            className="tool-panel-slider"
            trigger={null}
            collapsedWidth="20"
            width="300"
            collapsible
            collapsed={this.state.collapsed}
          >
            <div className="tool-panel-wrapper">
              <ToolPanel {...this.props} />
              <div className="ag-grid-tool-panel--buttons">
                <button
                  className="ag-grid-tool-panel--button"
                  onClick={this.toggleToolPanel}
                >
                  Columns
                </button>
              </div>
            </div>
          </Sider>
        </Layout>
        <InfoBar selectedCount={this.props.selectedCount} />
      </React.Fragment>
    );
  }
}